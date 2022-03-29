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
    url: String,
    useCache: Boolean = false,
    playingInitial: Boolean = true,
    soundInitial: Boolean = false
) :
    SKComponent<SKVideoVC>() {

    override val view: SKVideoVC = skvideoViewInjector.skVideo(
        url = url,
        useCache = useCache,
        playingInitial = playingInitial,
        soundInitial = soundInitial
    )

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
}