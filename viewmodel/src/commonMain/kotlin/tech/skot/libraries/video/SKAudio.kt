package tech.skot.libraries.video

import tech.skot.model.SKData
import tech.skot.model.SKManualData

class SKAudio(private val proxy:SKAudioVC) {

    fun setPlayList(tracks:List<SKAudioVC.Track>) {
        proxy.media = tracks
    }

    fun play() {
        proxy.playing = true
    }

    fun pause() {
        proxy.playing = false
    }

    fun setCurrentTrack(track: SKAudioVC.Track) {
        proxy.setCurrentTrack(track)
    }

    fun setProgressRefreshInterval(ms:Long) {
        proxy.progressRefreshInterval = ms
    }
    init {
        setProgressRefreshInterval(1000L)
    }

    private val _state = SKManualData<SKAudioVC.State?>(null)
    val state:SKData<SKAudioVC.State?> = _state

    private val _durations = SKManualData<Map<SKAudioVC.Track,Long>?>(null)
    val durations:SKData<Map<SKAudioVC.Track,Long>?> = _durations

    init {
        proxy.onState = {
            _state.value = it
        }
        proxy.onDurations = {
            _durations.value = it
        }
    }


}