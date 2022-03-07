package tech.skot.libraries.video

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import tech.skot.core.SKLog

open class SKAudioService : Service() {

    companion object {
        const val ACTION_FOREGROUND = "tech.skot.libraries.video.foreground"
        const val ACTION_BACKGROUND = "tech.skot.libraries.video.background"


    }

    private val CHANNEL_ID = "${this::class.simpleName}_sk-video"

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onCreate() {
        super.onCreate()
        SKLog.d("--------- SKAudioService    onCreate")
        skAudioViewProxy.let {
            if (it == null) {
                skAudioViewProxy = SKAudioViewProxy(applicationContext)
            }
            else {
                it.renewIfNeeded(applicationContext)
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            ACTION_BACKGROUND -> {
                showNotification()
            }
            ACTION_FOREGROUND -> {
                stopForeground(true)
            }
        }
        return START_STICKY
    }


    private fun createChannel(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mNotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel =
                NotificationChannel(
                    CHANNEL_ID, "Notifications audio",
                    NotificationManager.IMPORTANCE_MIN
                )
            notificationChannel.enableLights(false)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            mNotificationManager.createNotificationChannel(notificationChannel)
            CHANNEL_ID
        } else {
            ""
        }

    }

    private fun showNotification() {
        if (skAudioViewProxy?.player?.let { it.isPlaying } == true) {
            val notification = NotificationCompat.Builder(this, createChannel()).apply {
                setContentTitle("${skAudioViewProxy?.state?.track?.title}")
                setOngoing(true)
                setWhen(0)
                setSmallIcon(android.R.drawable.presence_audio_online)
                priority = NotificationCompat.PRIORITY_LOW
            }.build()

            startForeground(1, notification)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        SKLog.d("--------- SKAudioService    onDestroy")
        serviceJob.cancel()
        skAudioViewProxy?.release()

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


}