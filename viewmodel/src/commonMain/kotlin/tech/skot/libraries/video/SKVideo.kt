package tech.skot.libraries.video

import tech.skot.core.components.SKComponent
import tech.skot.libraries.video.di.skvideoViewInjector

/**
 * A Video Component
 * Based on ExoPlayer for Android
 * no iOs Version at this time
 *
 * @param url url of video you want play
 * @param playingInitial indicate if the video should play automatically when ready
 * @param soundInitial indicate if the soud should be on or of
 *
 * @property playing indicate if the video should play, play if true, pause if false
 *
 */
class SKVideo(
    videoInitial: SKVideoVC.VideoItem?,
    useCache: Boolean = false,
    playingInitial: Boolean = true,
    soundInitial: Boolean = false,
    repeatInitial: Boolean = true,
    onFullScreen: ((fullScreen: Boolean) -> Unit)? = null,
    onControllerVisibility: ((visible: Boolean) -> Unit)? = null,
) :
    SKComponent<SKVideoVC>() {

    constructor(
        urlInitial: String?,
        useCache: Boolean = false,
        playingInitial: Boolean = true,
        soundInitial: Boolean = false,
        repeatInitial: Boolean = true,
        onFullScreen: ((fullScreen: Boolean) -> Unit)? = null,
        onControllerVisibility: ((visible: Boolean) -> Unit)? = null,
    ) : this(
        videoInitial = urlInitial?.let { SKVideoVC.VideoItem(it) },
        useCache = useCache,
        playingInitial = playingInitial,
        soundInitial = soundInitial,
        repeatInitial = repeatInitial,
        onFullScreen = onFullScreen,
        onControllerVisibility = onControllerVisibility
    )


    override val view: SKVideoVC = skvideoViewInjector.skVideo(
        video = videoInitial,
        useCache = useCache,
        onFullScreen = onFullScreen,
        onControllerVisibility = onControllerVisibility,
        playingInitial = playingInitial,
        soundInitial = soundInitial,
        repeatInitial = repeatInitial
    )

    var video: SKVideoVC.VideoItem? = videoInitial
        set(value) {
            field = value
            view.video = value
        }

    var url: String?
        get() = video?.url
        set(value) {
            video = value?.let { SKVideoVC.VideoItem(it) }
        }

    var playing: Boolean = playingInitial
        set(value) {
            field = value
            view.playing = value
        }

    var sound: Boolean = soundInitial
        set(value) {
            field = value
            view.sound = value
        }

    fun onPause() {
        view.onPause()
    }

    fun onResume() {
        view.onResume()
    }

    fun setCurrentPosition(position: Long) {
        view.setCurrentPosition(position)
    }

    val currentPosition: Long?
        get() = view.currentPosition

}