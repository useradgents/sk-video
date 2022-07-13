package tech.skot.libraries.video

import android.app.Application
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import tech.skot.core.SKLog

fun Application.bindAudioService(audioService: Class<out SKAudioService>) {

    ProcessLifecycleOwner.get().lifecycle.addObserver(
        object : DefaultLifecycleObserver {

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                startService(Intent(applicationContext, audioService).apply {
                    action = SKAudioService.ACTION_FOREGROUND
                })

            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                startService(Intent(applicationContext, audioService).apply {
                    action = SKAudioService.ACTION_BACKGROUND
                })
            }


        }
    )
}