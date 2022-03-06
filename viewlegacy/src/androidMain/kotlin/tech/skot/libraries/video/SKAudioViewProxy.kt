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

    var state: SKAudioVC.State = SKAudioVC.State(track = null, playing = false, position = null, duration = null)
        set(value) {
            field = value
            onState?.invoke(value)
        }


    private fun ExoPlayer.updateState() {
        val track = currentMediaItem?.let { mapMediaItemTrack[it] }
        state = SKAudioVC.State(
            track = track,
            playing = _playing,
            position = track?.let { player.currentPosition }?.let { if (it>0) it else null },
            duration = track?.let { player.duration }?.let { if (it>0) it else null },
        )
    }


    val player = ExoPlayer.Builder(applicationContext)
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
                                val duration = timeline.getPeriod(it, Timeline.Period()).durationMs
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
            player.addMediaItems(
                value.map { track ->
                    MediaItem.Builder()
                        .setUri(track.url)
//                        .setTag(track.title)
//                        .setMediaId(track.url + track.hashCode().toString())
                        .build()
                        .also {
                            mapMediaItemTrack[it] = track
                        }
                }
            )
            player.prepare()
            player.pause()
            player.seekTo(0, 0)
            _playing = false
            player.updateState()
        }

    override fun setCurrentTrack(track:SKAudioVC.Track) {
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


    override fun destroy() {
        player.release()
    }

}