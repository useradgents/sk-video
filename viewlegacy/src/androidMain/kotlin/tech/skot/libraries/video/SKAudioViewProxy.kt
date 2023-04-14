package tech.skot.libraries.video

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import kotlinx.coroutines.*
import tech.skot.core.SKLog
import tech.skot.core.di.get
import java.io.File

var skAudioViewProxy: SKAudioViewProxy? = null

class SKAudioViewProxy(private val applicationContext: Context) : SKAudioVC {

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

    private fun getCacheDataSourceFactory(): DataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(Cache.getCache(get()))
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
    }

    private fun buildPlayer(applicationContext: Context): ExoPlayer =
        ExoPlayer.Builder(applicationContext)
            .setMediaSourceFactory(DefaultMediaSourceFactory(getCacheDataSourceFactory()))
            .build()
            .apply {
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
            } else {
                player.pause()
            }

        }


    private var _media: List<SKAudioVC.Track> = emptyList()
    override var trackList: List<SKAudioVC.Track>
        get() = _media
        set(value) {
            _media = value
            player.clearMediaItems()
            mapMediaItemTrack.clear()
            setMediaListToPlayer(value)
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


    override fun hasNext():Boolean {
        return player.hasNextMediaItem()
    }

    override fun seekToLastTrack() {
        if (hasNext()) {
            player.seekTo(player.mediaItemCount - 1, 0)
            player.updateState()
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
    }

    private fun SKAudioVC.Track.toMediaItem() = when(this){
        is SKAudioVC.Track.DistantTrack -> MediaItem.Builder()
            .setUri(uri)
            .build()
        is SKAudioVC.Track.LocalTrack -> MediaItem.Builder()
            .setUri(Uri.fromFile(File(uri)))
            .build()
    }

    override fun setCurrentTrack(track: SKAudioVC.Track) {
        val index = trackList.indexOf(track)
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
    }

}