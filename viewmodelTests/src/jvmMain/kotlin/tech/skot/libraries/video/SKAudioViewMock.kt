package tech.skot.libraries.video

class SKAudioViewMock : SKAudioVC {

    override var playing: Boolean = false
    override var trackList: List<SKAudioVC.Track> = emptyList()
    override var onState: ((state: SKAudioVC.State) -> Unit)? = null
    override var onDurations: ((durations: Map<SKAudioVC.Track, Long>) -> Unit)? = null
    override var keepActiveInBackGroundWithMessageIfNothingPlayed: String? = null
    override var progressRefreshInterval: Long = 1000L

    override fun setCurrentTrack(track: SKAudioVC.Track) {

    }

    override fun setCurrentTrack(index: Int) {
    }

    override fun addTrack(track: SKAudioVC.Track) {
    }

    var hasNextReturnValue:Boolean = false
    override fun hasNext(): Boolean {
        return hasNextReturnValue
    }

    var hasPreviousReturnValue:Boolean = false
    override fun hasPrevious(): Boolean {
        return hasPreviousReturnValue
    }

    override fun seekToLastTrack() {

    }

    override fun setNextTrack() {
    }

    override fun setPreviousTrack() {
    }

    override fun release() {

    }

    override fun seekToPosition(position: Long) {

    }

    override fun seekForward() {

    }

    override fun seekBack() {

    }
}