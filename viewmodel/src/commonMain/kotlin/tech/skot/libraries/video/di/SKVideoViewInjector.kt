package tech.skot.libraries.video.di

import tech.skot.core.di.get
import tech.skot.libraries.video.SKAudio
import tech.skot.libraries.video.SKAudioVC
import tech.skot.libraries.video.SKVideoVC

interface SKVideoViewInjector {
    fun skVideo(
        url: String,
        useCache : Boolean,
        playingInitial: Boolean,
        soundInitial: Boolean
    ): SKVideoVC

    fun skAudio(): SKAudioVC?
}

val skvideoViewInjector: SKVideoViewInjector = get()

val skAudio: SKAudio? = skvideoViewInjector.skAudio()?.let { SKAudio(it) }