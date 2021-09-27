package tech.skot.libraries.video.di

import tech.skot.core.di.get
import tech.skot.libraries.video.SKVideoVC

interface SKVideoViewInjector {
    fun skVideo(
        url: String,
        playingInitial: Boolean,
        soundInitial: Boolean
    ): SKVideoVC
}

val skvideoViewInjector: SKVideoViewInjector = get()