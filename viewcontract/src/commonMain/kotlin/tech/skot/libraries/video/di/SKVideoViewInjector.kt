package tech.skot.libraries.video.di

import tech.skot.core.di.get
import tech.skot.libraries.video.SKAudioVC
import tech.skot.libraries.video.SKVideoVC

interface SKVideoViewInjector {
    fun skVideo(
        url: String?,
        useCache: Boolean,
        playingInitial: Boolean,
        soundInitial: Boolean,
        onFullScreen:((fullScreen:Boolean)->Unit)?
    ): SKVideoVC

    fun skAudio(): SKAudioVC?
}

val skvideoViewInjector: SKVideoViewInjector = get()