package tech.skot.libraries.video.di

import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.libraries.video.SKAudioVC
import tech.skot.libraries.video.SKVideoViewProxy
import tech.skot.libraries.video.skAudioViewProxy

class SKVideoViewInjectorImpl : SKVideoViewInjector {
    override fun skVideo(
        url: String?,
        useCache: Boolean,
        playingInitial: Boolean,
        soundInitial: Boolean,
        onFullScreen:((fullScreen:Boolean)->Unit)?
    ) = SKVideoViewProxy(url, useCache, onFullScreen, playingInitial, soundInitial)

    override fun skAudio(): SKAudioVC? {
        return skAudioViewProxy
    }

}

val skvideoModule = module<BaseInjector> {
    single { SKVideoViewInjectorImpl() as SKVideoViewInjector }
}