package tech.skot.libraries.video

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import tech.skot.core.components.SKActivity

abstract class SKAudioActivity(private val audioService: Class<out SKAudioService>) : SKActivity() {
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {

        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }

    }

    override fun onStart() {
        super.onStart()
        startService(Intent(applicationContext, audioService).apply {
            action = SKAudioService.ACTION_FOREGROUND
        })
    }

    override fun onStop() {
        super.onStop()
        startService(Intent(applicationContext, audioService).apply {
            action = SKAudioService.ACTION_BACKGROUND
        })
    }

}