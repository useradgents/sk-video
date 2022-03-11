package tech.skot.libraries.video

import android.content.Intent
import tech.skot.core.components.SKActivity

abstract class SKAudioActivity(private val audioService: Class<out SKAudioService>) : SKActivity() {

    override fun onStart() {
        super.onStart()
        startService(Intent(applicationContext, audioService).apply {
            action = SKAudioService.ACTION_FOREGROUND
        })
    }


    override fun onStop() {
        startService(Intent(applicationContext, audioService).apply {
            action = SKAudioService.ACTION_BACKGROUND
        })
        super.onStop()

    }

}