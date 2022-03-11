package tech.skot.libraries.video

class SKAudioViewMock : SKAudioVC {

    override var playing: Boolean = false
    override var trackList: List<SKAudioVC.Track> = emptyList()
    override var onState: ((state: SKAudioVC.State) -> Unit)? = null
    override var onDurations: ((durations: Map<SKAudioVC.Track, Long>) -> Unit)? = null

    override var progressRefreshInterval: Long = 1000L

    override fun setCurrentTrack(track: SKAudioVC.Track) {

    }

    override fun addTrack(track: SKAudioVC.Track) {
    }

    var hasNextReturnValue:Boolean = false
    override fun hasNext(): Boolean {
        return hasNextReturnValue
    }

    override fun seekToLastTrack() {

    }

    override fun release() {

    }
}