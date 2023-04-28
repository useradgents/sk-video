package tech.skot.libraries.video

import tech.skot.core.components.SKComponentViewMock

class SKVideoViewMock(
    videoInitial: SKVideoVC.VideoItem?,
    override val useCache: Boolean,
    override val onFullScreen:((fullScreen:Boolean)->Unit)?,
    override val onControllerVisibility: ((visible: Boolean) -> Unit)?,
    playingInitial: Boolean,
    soundInitial: Boolean,
    repeatInitial: Boolean
) : SKComponentViewMock(), SKVideoVC {
    override var playing: Boolean = playingInitial
    override var sound: Boolean = soundInitial
    override var video: SKVideoVC.VideoItem? = videoInitial
    override var repeat: Boolean = repeatInitial


    override fun setCurrentPosition(position: Long) {

    }

    override val currentPosition: Long?
        get() = null

    override fun onPause() {
    }

    override fun onResume() {
    }
}