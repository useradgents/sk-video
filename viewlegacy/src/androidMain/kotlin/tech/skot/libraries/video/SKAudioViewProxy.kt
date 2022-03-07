package tech.skot.libraries.video

import android.content.Context
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import kotlinx.coroutines.*
import tech.skot.core.SKLog

var skAudioViewProxy: SKAudioViewProxy? = null

class SKAudioViewProxy(applicationContext: Context) : SKAudioVC {

    override var progressRefreshInterval: Long = 1000L

    private val mapMediaItemTrack: MutableMap<MediaItem, SKAudioVC.Track> = mutableMapOf()

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
            duration = track?.let { player.currentDuration()},
        )
    }

    private fun ExoPlayer.currentDuration() = player.duration.let { if (it > 0) it else null }

    var _player: ExoPlayer? = buildPlayer(applicationContext)

    val player: ExoPlayer
        get() = _player ?: throw IllegalStateException("Player released")

    fun renewIfNeeded(applicationContext: Context) {
        SKLog.d("@SKAudio    renewIfNeeded   -----> _player $_player")
        if (_player == null) {
            SKLog.d("@SKAudio    renew   -----> media $media")
            SKLog.d("@SKAudio    renew   -----> state $state")
            val toRestoredTrack = state.track
            _player = buildPlayer(applicationContext)
            setMediaListToPlayer(media)
            if (toRestoredTrack != null) {
                player.seekTo(media.indexOf(toRestoredTrack), savedPosition)
                state = SKAudioVC.State(track = toRestoredTrack, playing = _playing, position = savedPosition, duration = saveDuration)

            }
            else {
                player.seekTo(0, 0)
            }

        }
    }

    private fun buildPlayer(applicationContext: Context): ExoPlayer =
        ExoPlayer.Builder(applicationContext)
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


    override var media: List<SKAudioVC.Track> = emptyList()
        set(value) {
            field = value
            player.clearMediaItems()
            mapMediaItemTrack.clear()
            setMediaListToPlayer(value)
            player.pause()
            player.seekTo(0, 0)
            _playing = false
            player.updateState()
        }

    private fun setMediaListToPlayer(media: List<SKAudioVC.Track>) {
        player.addMediaItems(
            media.map { track ->
                MediaItem.Builder()
                    .setUri(track.url)
                    .build()
                    .also {
                        mapMediaItemTrack[it] = track
                    }
            }
        )
        player.prepare()
        player.pause()
    }

    override fun setCurrentTrack(track: SKAudioVC.Track) {
        val index = media.indexOf(track)
        if (index != -1) {
            player.seekTo(index, 0)
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


    private var savedPosition:Long = 0
    private var saveDuration:Long = 0
    override fun release() {
        savedPosition = _player?.currentPosition ?: 0L
        saveDuration = player.contentDuration
        _player?.release()
        _player = null
    }

}