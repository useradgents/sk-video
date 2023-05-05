package tech.skot.libraries.video


/**
 *
 *
 */
interface SKAudioVC {

    data class Track(
        val uri: () -> String,
        val title: String,
        val image: String?,
    )


    data class State(
        val track: Track?,
        val playing: Boolean,
        val position: Long?,
        val duration: Long?
    )

    var playing: Boolean
    var trackList: List<Track>

    var keepActiveInBackGroundWithMessageIfNothingPlayed: String?

    fun addTrack(track: Track)
    fun hasNext(): Boolean
    fun hasPrevious(): Boolean
    fun seekToLastTrack()

    fun setNextTrack()
    fun setPreviousTrack()

    var progressRefreshInterval: Long

    var onState: ((state: State) -> Unit)?
    var onDurations: ((durations: Map<Track, Long>) -> Unit)?

    fun setCurrentTrack(track: Track)
    fun setCurrentTrack(index: Int)

    fun release()
    fun seekToPosition(position: Long)
    fun seekForward()
    fun seekBack()

}