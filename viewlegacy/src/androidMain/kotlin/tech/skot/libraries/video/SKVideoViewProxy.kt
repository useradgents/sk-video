package tech.skot.libraries.video

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.util.EventLogger
import androidx.media3.ui.PlayerView
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.di.get
import tech.skot.view.live.SKMessage
import java.io.File


@UnstableApi
class SKVideoViewProxy(
    videoInitial: SKVideoVC.VideoItem?,
    override val useCache: Boolean,
    override val onFullScreen: ((fullScreen: Boolean) -> Unit)?,
    override val onControllerVisibility: ((visible: Boolean) -> Unit)?,
    playingInitial: Boolean,
    soundInitial: Boolean,
    repeatInitial : Boolean,
) : SKComponentViewProxy<PlayerView>(), SKVideoVC {


    var player: ExoPlayer? = null


    private fun getCacheDataSourceFactory(): DataSource.Factory {
        return CacheDataSource.Factory()
            .setCache(Cache.getCache(get()))
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())
    }

    private fun buildPlayer(playNow: Boolean, positionMs: Long, withSound: Boolean): ExoPlayer {
        val builder = ExoPlayer.Builder(get())
        if (useCache) {
            builder.setMediaSourceFactory(DefaultMediaSourceFactory(getCacheDataSourceFactory()))
        }
        return builder.build().apply {
            this.addAnalyticsListener(EventLogger())

            seekTo(currentMediaItemIndex, positionMs)
            playWhenReady = playNow
            repeatMode =  if(repeat) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
            volume = if (withSound) 1f else 0f

            video?.let { video ->
                val mediaBuilder = MediaItem.Builder().setUri(video.url)
                video.mimeType?.let { mediaBuilder.setMimeType(it) }
                this.addMediaItem(mediaBuilder.build())
            }
            prepare()
        }
    }


    override var sound: Boolean = soundInitial
        set(value) {
            field = value
            player?.volume = if (value) 1f else 0f
        }


    override var playing: Boolean = playingInitial
        set(value) {
            field = value
            updatePlaying()
        }

    override var repeat : Boolean = repeatInitial
        set(value) {
            field = value
            player?.repeatMode = if(value) Player.REPEAT_MODE_ALL else Player.REPEAT_MODE_OFF
            updatePlaying()
        }

    override var video: SKVideoVC.VideoItem? = videoInitial
        set(value) {
            field = value
            updatePlayerUrl()
        }

    private val setCurrentPositionMessage = SKMessage<Long>()

    override fun setCurrentPosition(position: Long) {
        setCurrentPositionMessage.post(position)
    }

    override val currentPosition: Long?
        get() = player?.currentPosition

//    override val isPlaying: Boolean
//        get() = player?.isPlaying ?: false

    private var savedPosition: Long = 1
    private var resumed: Boolean = true
    override fun onPause() {
        resumed = false
        savedPosition = player?.currentPosition ?: 1
        updatePlaying()
    }

    override fun onResume() {
        resumed = true
        updatePlaying()
    }

    fun updatePlaying() {
        if (resumed && playing) {
            player?.play()
        } else {
            player?.pause()
        }
    }

    fun updatePlayerUrl() {
        player?.let {
            it.stop()
            it.clearMediaItems()
            video?.let { video ->
                val builder = MediaItem.Builder().setUri(video.url)
                video.mimeType?.let { builder.setMimeType(it) }
                it.addMediaItem(builder.build())
            }
            it.prepare()
            it.play()
        }

    }

    override fun onRemove() {
        super.onRemove()
        player?.release()

    }


    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: PlayerView,
    ): SKVideoView {
        return (player ?: buildPlayer(
            playNow = playing,
            positionMs = savedPosition,
            withSound = sound
        ).also { player = it }).let { thePlayer ->
            SKVideoView(this, activity, fragment, binding, thePlayer).apply {
                setOnFullScreen(onFullScreen)
                setOnControllerVisibility(onControllerVisibility)
                setCurrentPositionMessage.observe {
                    setCurrentPosition(it)
                }
            }
        }
    }
}


/**
 * singleton for cache because only one [SimpleCache] is allowed by directory
 */
@UnstableApi
object Cache {
    private var cache: SimpleCache? = null
    fun getCache(context: Context): SimpleCache {
        return cache ?: kotlin.run {
            val cacheEvictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024)
            val exoplayerDatabaseProvider = StandaloneDatabaseProvider(context)
            val cacheDir = File(context.cacheDir,"skvideoCache").apply {
                if (!this.exists() || !this.isDirectory ){
                    this.delete()
                    this.mkdirs()
                }
            }
            SimpleCache(cacheDir, cacheEvictor, exoplayerDatabaseProvider).apply {
                cache = this
            }
        }
    }
}