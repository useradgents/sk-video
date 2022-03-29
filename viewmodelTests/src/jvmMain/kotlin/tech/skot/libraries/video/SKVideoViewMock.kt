package tech.skot.libraries.video

import tech.skot.core.components.SKComponentViewMock

class SKVideoViewMock(
    override val url: String,
    override val useCache: Boolean,
    playingInitial: Boolean,
    soundInitial: Boolean
) : SKComponentViewMock(), SKVideoVC {
    override var playing: Boolean = playingInitial
    override var sound: Boolean = soundInitial

    override fun onPause() {
    }

    override fun onResume() {
    }
}