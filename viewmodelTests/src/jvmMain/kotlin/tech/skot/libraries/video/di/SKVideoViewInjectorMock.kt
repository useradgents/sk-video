package tech.skot.libraries.video.di

import tech.skot.core.di.InjectorMock
import tech.skot.core.di.module
import tech.skot.libraries.video.SKAudioVC
import tech.skot.libraries.video.SKAudioViewMock
import tech.skot.libraries.video.SKVideoViewMock

class SKVideoViewInjectorMock : SKVideoViewInjector {
    override fun skVideo(url: String, playingInitial: Boolean, soundInitial: Boolean) =
        SKVideoViewMock(url, playingInitial, soundInitial)

    override fun skAudio() = SKAudioViewMock()
}

val skvideoModuleMock = module<InjectorMock> {
    single<SKVideoViewInjector> { SKVideoViewInjectorMock() }
}