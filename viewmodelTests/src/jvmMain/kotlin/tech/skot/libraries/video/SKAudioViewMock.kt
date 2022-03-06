package tech.skot.libraries.video

class SKAudioViewMock: SKAudioVC {
    override var playing: Boolean
        get() = TODO("Not yet implemented")
        set(value) {}
    override var media: List<SKAudioVC.Track>
        get() = TODO("Not yet implemented")
        set(value) {}
    override var onState: ((state: SKAudioVC.State) -> Unit)?
        get() = TODO("Not yet implemented")
        set(value) {}
    override var onDurations: ((durations: Map<SKAudioVC.Track, Long>) -> Unit)?
        get() = TODO("Not yet implemented")
        set(value) {}

    override var progressRefreshInterval: Long = 1000L

    override fun setCurrentTrack(track: SKAudioVC.Track) {
        TODO("Not yet implemented")
    }
    override fun destroy() {
        TODO("Not yet implemented")
    }
}