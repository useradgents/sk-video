package tech.skot.libraries.video.di

import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.libraries.video.SKVideoViewProxy

class SKVideoViewInjectorImpl : SKVideoViewInjector {
    override fun skVideo(
        url: String,
        playingInitial: Boolean,
        soundInitial: Boolean
    ) = SKVideoViewProxy(url, playingInitial, soundInitial)
}

val skvideoModule = module<BaseInjector> {
    single { SKVideoViewInjectorImpl() as SKVideoViewInjector }
}