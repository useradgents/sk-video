package tech.skot.libraries.video.di

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import tech.skot.core.di.BaseInjector
import tech.skot.core.di.module
import tech.skot.libraries.video.SKAudioVC
import tech.skot.libraries.video.SKVideoVC
import tech.skot.libraries.video.SKVideoViewProxy
import tech.skot.libraries.video.skAudioViewProxy

class SKVideoViewInjectorImpl : SKVideoViewInjector {
    @OptIn(UnstableApi::class)
    override fun skVideo(
        videoInitial: SKVideoVC.VideoItem?,
        useCache: Boolean,
        playingInitial: Boolean,
        soundInitial: Boolean,
        repeatInitial: Boolean,
        onFullScreen:((fullScreen:Boolean)->Unit)?,
        onControllerVisibility: ((visible: Boolean) -> Unit)?
    ) = SKVideoViewProxy(videoInitial, useCache, onFullScreen, onControllerVisibility, playingInitial, soundInitial, repeatInitial)

    override fun skAudio(): SKAudioVC? {
        return skAudioViewProxy
    }

}

val skvideoModule = module<BaseInjector> {
    single { SKVideoViewInjectorImpl() as SKVideoViewInjector }
}