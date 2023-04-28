package tech.skot.libraries.video.di

import tech.skot.core.di.get
import tech.skot.libraries.video.SKAudioVC
import tech.skot.libraries.video.SKVideoVC

interface SKVideoViewInjector {
    fun skVideo(
        video: SKVideoVC.VideoItem?,
        useCache: Boolean,
        playingInitial: Boolean,
        soundInitial: Boolean,
        repeatInitial: Boolean,
        onFullScreen:((fullScreen:Boolean)->Unit)?,
        onControllerVisibility: ((visible: Boolean) -> Unit)?
    ): SKVideoVC

    fun skAudio(): SKAudioVC?
}

val skvideoViewInjector: SKVideoViewInjector = get()