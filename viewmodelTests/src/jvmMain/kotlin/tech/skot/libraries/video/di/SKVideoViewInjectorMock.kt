package tech.skot.libraries.video.di

import tech.skot.core.di.InjectorMock
import tech.skot.core.di.module
import tech.skot.libraries.video.SKAudioViewMock
import tech.skot.libraries.video.SKVideoVC
import tech.skot.libraries.video.SKVideoViewMock

class SKVideoViewInjectorMock : SKVideoViewInjector {
    override fun skVideo(
        video: SKVideoVC.VideoItem?,
        useCache: Boolean,
        playingInitial: Boolean,
        soundInitial: Boolean,
        repeatInitial : Boolean,
        onFullScreen: ((fullScreen:Boolean)->Unit)?,
        onControllerVisibility: ((visible: Boolean) -> Unit)?
    ) =
        SKVideoViewMock(video, useCache, onFullScreen, onControllerVisibility, playingInitial, soundInitial, repeatInitial)

    override fun skAudio() = SKAudioViewMock()
}

val skvideoModuleMock = module<InjectorMock> {
    single<SKVideoViewInjector> { SKVideoViewInjectorMock() }
}