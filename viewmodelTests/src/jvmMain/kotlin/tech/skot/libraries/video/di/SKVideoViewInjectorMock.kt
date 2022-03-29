package tech.skot.libraries.video.di

import tech.skot.core.di.InjectorMock
import tech.skot.core.di.module
import tech.skot.libraries.video.SKAudioViewMock
import tech.skot.libraries.video.SKVideoViewMock

class SKVideoViewInjectorMock : SKVideoViewInjector {
    override fun skVideo(
        url: String,
        useCache: Boolean,
        playingInitial: Boolean,
        soundInitial: Boolean
    ) =
        SKVideoViewMock(url, useCache, playingInitial, soundInitial)

    override fun skAudio() = SKAudioViewMock()
}

val skvideoModuleMock = module<InjectorMock> {
    single<SKVideoViewInjector> { SKVideoViewInjectorMock() }
}