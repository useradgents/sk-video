package tech.skot.libraries.video

import android.content.Context
import android.net.Uri
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import kotlinx.coroutines.*
import tech.skot.core.SKLog
import tech.skot.core.di.get
import java.io.File

var skAudioViewProxy: SKAudioViewProxy? = null

class SKAudioViewProxy(private val applicationContext: Context) : SKAudioVC {

    var mediaSession: MediaSessionCompat? = null
    override var progressRefreshInterval: Long = 1000L

    private val mapMediaItemTrack: MutableMap<MediaItem, SKAudioVC.Track> = mutableMapOf()

    override var keepActiveInBackGroundWithMessageIfNothingPlayed: String? = null


    var state: SKAudioVC.State =
        SKAudioVC.State(track = null, playing = false, position = null, duration = null)
        set(value) {
            field = value
            onState?.invoke(value)
        }


    private fun ExoPlayer.updateState() {
        val track = currentMediaItem?.let { mapMediaItemTrack[it] }
        state = SKAudioVC.State(
            track = track,
            playing = _playing,
            position = track?.let { player.currentPosition }?.let { if (it > 0) it else null },
            duration = track?.let { player.currentDuration() },
        )
    }

    private fun ExoPlayer.currentDuration() = player.duration.let { if (it > 0) it else null }

    var _player: ExoPlayer? = null//buildPlayer(applicationContext)

    val player: ExoPlayer
        get() {
            SKLog.d("@-------------- SKAudioViewProxy player  $_player")
            return _player ?: buildPlayer(applicationContext).also {
                SKLog.d("--- rebuilded")
                _player = it
            }
        }

    fun renewIfNeeded(applicationContext: Context) {
        if (_player == null) {
            val toRestoredTrack = state.track
            _player = buildPlayer(applicationContext)
            setMediaListToPlayer(trackList)
            if (toRestoredTrack != null) {
                player.seekTo(trackList.indexOf(toRestoredTrack), savedPosition)
                state = SKAudioVC.State(
                    track = toRestoredTrack,
                    playing = _playing,
                    position = savedPosition,
                    duration = saveDuration
                )

            } else {
                player.seekTo(0, 0)
            }

        }
    }

    private fun getCacheDataSourceFactory(applicationContext: Context): DataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(Cache.getCache(get()))
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(applicationContext))
    }

    private fun buildPlayer(applicationContext: Context): ExoPlayer =
        ExoPlayer.Builder(applicationContext)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(
                    getCacheDataSourceFactory(
                        applicationContext
                    )
                )
            )
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build()
            .apply {
                mediaSession?.release()
                mediaSession = null

                val session = MediaSessionCompat(applicationContext, applicationContext.packageName)
                session.setCallback(object : MediaSessionCompat.Callback() {
                    override fun onSkipToNext() {
                        super.onSkipToNext()
                        skAudioViewProxy?.setNextTrack()
                    }

                    override fun onSkipToPrevious() {
                        super.onSkipToPrevious()
                        skAudioViewProxy?.setPreviousTrack()
                    }


                })
                mediaSession = session

                val mediaSessionConnector = MediaSessionConnector(session)
                mediaSessionConnector.setPlayer(this)
                mediaSessionConnector.setQueueNavigator(object : TimelineQueueNavigator(session) {
                    @Override
                    override fun getMediaDescription(
                        player: Player,
                        windowIndex: Int
                    ): MediaDescriptionCompat {
                        return MediaDescriptionCompat.Builder().apply {
                            setTitle("${trackList.get(windowIndex).title}")
                            setDescription("MediaDescription ${trackList.get(windowIndex).title}")
                            setSubtitle("subtitle${trackList.get(windowIndex).title}")
                        }.build()
                    }
                });
                mediaSessionConnector.setEnabledPlaybackActions(
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PAUSE
                            or PlaybackStateCompat.ACTION_SEEK_TO
                            or PlaybackStateCompat.ACTION_FAST_FORWARD
                            or PlaybackStateCompat.ACTION_REWIND
                            or PlaybackStateCompat.ACTION_STOP
                )

                addListener(object : Player.Listener {
                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        super.onTimelineChanged(timeline, reason)
                        onDurations?.let { onDurations ->
                            val mapTrackDuration: MutableMap<SKAudioVC.Track, Long> = mutableMapOf()
                            (0 until timeline.periodCount).forEach {
                                val mediaItem = getMediaItemAt(it)
                                mapMediaItemTrack[mediaItem]?.let { track ->
                                    val duration =
                                        timeline.getPeriod(it, Timeline.Period()).durationMs
                                    if (duration > 0) {
                                        mapTrackDuration[track] = duration
                                    }
                                }
                            }
                            onDurations(mapTrackDuration)
                        }

                    }


                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        super.onMediaItemTransition(mediaItem, reason)
                        updateState()

                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        if (isPlaying) {
                            startProgressListener()
                            _playing = true
                        } else {
                            stopProgressListener()
                            _playing = false
                        }
                        updateState()
                    }


                })
            }

    private var _playing: Boolean = false
    override var playing: Boolean
        get() = _playing
        set(value) {
            _playing = value
            if (value) {
                player.play()
                mediaSession?.isActive = true
            } else {
                player.pause()
            }

        }

    override var sound: Boolean = player.volume == 1f
        get() = player.volume == 1f
        set(value) {
            field = value
            player.volume = if (value) 1f else 0f
        }


    private var _media: List<SKAudioVC.Track> = emptyList()
    override var trackList: List<SKAudioVC.Track>
        get() = _media
        set(value) {
            _media = value
            player.clearMediaItems()
            mapMediaItemTrack.clear()
            setMediaListToPlayer(value)
            mediaSession?.isActive = value.isNotEmpty()
            player.pause()
            player.seekTo(0, 0)
            _playing = false
            player.updateState()
        }


    override fun addTrack(track: SKAudioVC.Track) {
        if (trackList.isEmpty()) {
            trackList = listOf(track)
        } else {
            player.addMediaItem(
                track.toMediaItem().also { mapMediaItemTrack[it] = track }
            )
            _media = _media + track
        }
        player.updateState()
    }


    override fun hasNext(): Boolean {
        return player.hasNextMediaItem()
    }

    override fun hasPrevious(): Boolean {
        return player.hasPreviousMediaItem()
    }

    override fun seekToLastTrack() {
        if (hasNext()) {
            player.seekTo(player.mediaItemCount - 1, 0)
            player.updateState()
        }
    }

    override fun setNextTrack() {
        state.track?.let { currentTrack ->
            val index = _media.indexOf(currentTrack)
            if (hasNext()) {
                setCurrentTrack(index + 1)
            }
        }
    }

    override fun setPreviousTrack() {
        state.track?.let { currentTrack ->
            val index = _media.indexOf(currentTrack)
            if (hasPrevious()) {
                setCurrentTrack(index - 1)
            }
        }
    }


    private fun setMediaListToPlayer(media: List<SKAudioVC.Track>) {
        player.addMediaItems(
            media.map { track ->
                track.toMediaItem()
                    .also {
                        mapMediaItemTrack[it] = track
                    }
            }
        )
        player.prepare()
        player.pause()
        mediaSession?.isActive = true


    }

    private fun SKAudioVC.Track.toMediaItem(): MediaItem {
        val uri = uri()
        return if (uri.startsWith("/")) { //local file
            MediaItem.Builder()
                .setUri(Uri.fromFile(File(uri)))
                .build()
        } else {//distant file
            MediaItem.Builder()
                .setUri(uri)
                .build()
        }
    }


    override fun setCurrentTrack(track: SKAudioVC.Track) {
        setCurrentTrack(trackList.indexOf(track))

    }

    override fun setCurrentTrack(index: Int) {
        if (index != -1) {
            player.seekTo(index, 0)
            player.updateState()
        }
    }

    override var onState: ((state: SKAudioVC.State) -> Unit)? = null


    override var onDurations: ((durations: Map<SKAudioVC.Track, Long>) -> Unit)? = null


    private var progressListenerJob: Job? = null
    private fun startProgressListener() {
        progressListenerJob?.cancel()
        progressListenerJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                delay(progressRefreshInterval)
                player.updateState()
            }

        }
    }

    private fun stopProgressListener() {
        progressListenerJob?.cancel()
    }


    private var savedPosition: Long = 0
    private var saveDuration: Long = 0
    override fun release() {
        SKLog.d("@-------release  0")
        savedPosition = _player?.currentPosition ?: 0L
        SKLog.d("@-------release  1")
        saveDuration = _player?.contentDuration ?: 0L
        SKLog.d("@-------release  2")
        _player?.release()
        SKLog.d("@-------release  3")
        _player = null
        mediaSession?.isActive = false
        mediaSession?.release()
        mediaSession = null
    }

    override fun seekToPosition(position: Long) {
        player.seekTo(position)
        player.seekBackIncrement
        player.updateState()
    }

    override fun seekForward() {
        player.seekForward()
    }

    override fun seekBack() {
        player.seekBack()
    }

}