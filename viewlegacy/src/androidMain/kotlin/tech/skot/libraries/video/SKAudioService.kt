package tech.skot.libraries.video

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import tech.skot.core.components.SKActivity

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
        skAudioViewProxy.let {
            if (it == null) {
                skAudioViewProxy = SKAudioViewProxy(applicationContext)
            } else {
                it.renewIfNeeded(applicationContext)
            }
        }

    }


    private var foregroundCounter = 0
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_BACKGROUND -> {
                foregroundCounter--
                if (foregroundCounter <= 0) {
                    showNotification()
                }

            }
            ACTION_FOREGROUND -> {
                updateNotificationJob?.cancel()
                foregroundCounter++
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

    private fun pendingIntent(): PendingIntent {
        val notificationIntent = Intent(this, SKActivity.launchActivityClass)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    private fun buildNotification(): Notification? {
        val playingTrack = skAudioViewProxy?.state?.track

        return if (skAudioViewProxy?.keepActiveInBackGroundWithMessageIfNothingPlayed != null || playingTrack != null) {
            NotificationCompat.Builder(this, createChannel()).apply {
                setContentTitle(playingTrack?.title ?: skAudioViewProxy?.keepActiveInBackGroundWithMessageIfNothingPlayed ?: "---")
                setOngoing(true)
                setWhen(0)
                setContentIntent(pendingIntent())
                setSmallIcon(android.R.drawable.presence_audio_online)
                priority = NotificationCompat.PRIORITY_LOW
            }.build()
        } else {
            null
        }
    }


    var updateNotificationJob: Job? = null

    private fun showNotification() {
        updateNotificationJob?.cancel()
        buildNotification()?.let {
            startForeground(1, it)
            updateNotificationJob = serviceScope.launch {
                while (true) {
                    delay(1000)
                    buildNotification().let { notification ->
                        val notificationManager =
                            getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
                        if (notification == null) {
                            notificationManager?.cancel(1)
                            updateNotificationJob?.cancel()
                            this@SKAudioService.stopSelf()
                        } else {
                            notificationManager?.notify(1, notification)
                        }
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
        skAudioViewProxy?.release()

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


}