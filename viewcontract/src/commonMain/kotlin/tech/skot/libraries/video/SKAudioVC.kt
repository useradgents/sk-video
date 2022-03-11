package tech.skot.libraries.video


/**
 *
 *
 */
interface SKAudioVC {

    data class Track(val title:String, val url:String, val image:String?)
    data class State(val track:Track?, val playing:Boolean, val position:Long?, val duration:Long?)

    var playing: Boolean
    var trackList: List<Track>

    fun addTrack(track:Track)
    fun hasNext():Boolean
    fun seekToLastTrack()

    var progressRefreshInterval:Long

    var onState: ((state:State) -> Unit)?
    var onDurations: ((durations: Map<Track, Long>) -> Unit)?

    fun setCurrentTrack(track: Track)

    fun release()

}