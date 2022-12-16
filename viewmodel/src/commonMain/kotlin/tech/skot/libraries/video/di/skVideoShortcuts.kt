package tech.skot.libraries.video.di

import tech.skot.libraries.video.SKAudio

val skAudio: SKAudio? = skvideoViewInjector.skAudio()?.let { SKAudio(it) }