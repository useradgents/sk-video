package tech.skot.libraries.video

import tech.skot.model.SKData
import tech.skot.model.SKManualData

class SKAudio(private val proxy: SKAudioVC) {

    fun setPlayList(tracks: List<SKAudioVC.Track>) {
        proxy.trackList = tracks
    }

    fun addTrackIfNotIn(track: SKAudioVC.Track) {
        if (!proxy.trackList.contains(track)) {
            proxy.addTrack(track)
        }
    }

    fun play() {
        proxy.playing = true
    }

    var playing:Boolean
        get() = proxy.playing
    set(value) {
        proxy.playing = value
    }

    fun pause() {
        proxy.playing = false
    }

    fun setCurrentTrack(track: SKAudioVC.Track) {
        proxy.setCurrentTrack(track)
    }

    fun hasNext(): Boolean {
        return proxy.hasNext()
    }

    fun seekToLastTrack() {
        proxy.seekToLastTrack()
    }

    fun setProgressRefreshInterval(ms: Long) {
        proxy.progressRefreshInterval = ms
    }

    var keepActiveInBackGroundWithMessageIfNothingPlayed: String?
        get() = proxy.keepActiveInBackGroundWithMessageIfNothingPlayed
        set(value) {
            proxy.keepActiveInBackGroundWithMessageIfNothingPlayed = value
        }

    init {
        setProgressRefreshInterval(1000L)
    }

    private val _state = SKManualData<SKAudioVC.State?>(null)
    val state: SKData<SKAudioVC.State?> = _state

    val currentState:SKAudioVC.State?
        get() = _state.value

    private val _durations = SKManualData<Map<SKAudioVC.Track, Long>?>(null)
    val durations: SKData<Map<SKAudioVC.Track, Long>?> = _durations


    init {
        proxy.onState = {
            _state.value = it
        }
        proxy.onDurations = {
            _durations.value = it
        }
    }


}