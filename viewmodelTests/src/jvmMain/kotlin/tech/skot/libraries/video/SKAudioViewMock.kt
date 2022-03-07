package tech.skot.libraries.video

class SKAudioViewMock : SKAudioVC {

    override var playing: Boolean = false
    override var media: List<SKAudioVC.Track> = emptyList()
    override var onState: ((state: SKAudioVC.State) -> Unit)? = null
    override var onDurations: ((durations: Map<SKAudioVC.Track, Long>) -> Unit)? = null

    override var progressRefreshInterval: Long = 1000L

    override fun setCurrentTrack(track: SKAudioVC.Track) {

    }

    override fun release() {

    }
}