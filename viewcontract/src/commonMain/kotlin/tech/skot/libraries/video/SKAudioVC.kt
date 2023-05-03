package tech.skot.libraries.video


/**
 *
 *
 */
interface SKAudioVC {

    sealed class Track(){
        abstract val uri : String
        abstract val title:String
        abstract val image:String?
        data class DistantTrack(override val title:String, override val uri:String, override val image:String?) : Track()
        data class LocalTrack(override val title:String, override val uri:String,override val image:String?) : Track()
    }


    data class State(val track:Track?, val playing:Boolean, val position:Long?, val duration:Long?)

    var playing: Boolean
    var trackList: List<Track>

    var keepActiveInBackGroundWithMessageIfNothingPlayed:String?

    fun addTrack(track:Track)
    fun hasNext():Boolean
    fun seekToLastTrack()

    var progressRefreshInterval:Long

    var onState: ((state:State) -> Unit)?
    var onDurations: ((durations: Map<Track, Long>) -> Unit)?

    fun setCurrentTrack(track: Track)

    fun release()
    fun seekToPosition(position: Long)
    fun seekForward()
    fun seekBack()

}