package tech.skot.libraries.video

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.*
import tech.skot.core.SKLog
import tech.skot.core.components.SKActivity

open class SKAudioService : Service() {

    private val CHANNEL_ID = "${this::class.simpleName}_sk-video"

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    open val notificationIcon: Int = android.R.drawable.presence_audio_online

    override fun onCreate() {
        super.onCreate()
//        SKLog.d("@------------- SKAudioService  onCreate !!!")
        skAudioViewProxy.let {
//            SKLog.d("@------------- SKAudioService  onCreate !!!  skAudioViewProxy $it")
            if (it == null) {
                skAudioViewProxy = SKAudioViewProxy(applicationContext)
            } else {
                it.renewIfNeeded(applicationContext)
            }
        }

//        SKLog.d("@------------- SKAudioService  onCreate !!!  skAudioViewProxy $skAudioViewProxy")

        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {

                override fun onResume(owner: LifecycleOwner) {
                    updateNotificationJob?.cancel()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        stopForeground(STOP_FOREGROUND_REMOVE)
                    } else {
                        stopForeground(true)
                    }
                }

                override fun onStop(owner: LifecycleOwner) {
                    super.onStop(owner)
                    showNotification()
                }

            }
        )

    }


//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        return START_STICKY
//    }


    open fun createChannel(): String {
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

     fun pendingIntent(): PendingIntent {
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

    open fun buildNotification(): Notification? {
        val playingTrack = skAudioViewProxy?.state?.track

        return if (skAudioViewProxy?.keepActiveInBackGroundWithMessageIfNothingPlayed != null || playingTrack != null) {
            NotificationCompat.Builder(this, createChannel()).apply {
                setContentTitle(
                    playingTrack?.title
                        ?: skAudioViewProxy?.keepActiveInBackGroundWithMessageIfNothingPlayed
                        ?: "---"
                )
                    .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(
                        skAudioViewProxy?.mediaSession?.sessionToken))
                setOngoing(true)
                setWhen(0)
                setContentIntent(pendingIntent())
                setSmallIcon(notificationIcon)
                priority = NotificationCompat.PRIORITY_LOW
            }.build()
        } else {
            null
        }
    }


    var updateNotificationJob: Job? = null

    var playing = false

    private fun showNotification() {
        updateNotificationJob?.cancel()
        buildNotification()?.let {
            startForeground(1, it)
            updateNotificationJob = serviceScope.launch {
                while (true) {
                    delay(1000)
                    if (playing || skAudioViewProxy?.playing != false) {
                        playing = skAudioViewProxy?.playing?: false
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
    }


    override fun onDestroy() {
        super.onDestroy()
//        SKLog.d("@------------- SKAudioService  onDestroy !!! skAudioViewProxy $skAudioViewProxy")
        serviceJob.cancel()
        skAudioViewProxy?.release()

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


}