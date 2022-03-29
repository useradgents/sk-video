package tech.skot.libraries.video

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.di.get


class SKVideoViewProxy(
    override val url: String,
    override val useCache: Boolean,
    playingInitial: Boolean,
    soundInitial: Boolean
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
            seekTo(currentMediaItemIndex, positionMs)
            playWhenReady = playNow
            repeatMode = Player.REPEAT_MODE_ALL
            addMediaItem(MediaItem.fromUri(url))
            prepare()
            volume = if (withSound) 1f else 0f
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

    override fun onRemove() {
        super.onRemove()
        player?.release()
    }


    override fun bindTo(
        activity: SKActivity,
        fragment: Fragment?,
        binding: PlayerView
    ): SKVideoView {
        return (player ?: buildPlayer(
            playNow = playing,
            positionMs = savedPosition,
            withSound = sound
        ).also { player = it }).let { thePlayer ->
            SKVideoView(this, activity, fragment, binding, thePlayer)
        }
    }
}


/**
 * singleton for cache because only one [SimpleCache] is allowed by directory
 */
object Cache {
    private var cache: SimpleCache? = null
    fun getCache(context: Context): SimpleCache {
        return cache ?: kotlin.run {
            val cacheEvictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024)
            val exoplayerDatabaseProvider = StandaloneDatabaseProvider(context)
            SimpleCache(context.cacheDir, cacheEvictor, exoplayerDatabaseProvider).apply {
                cache = this
            }
        }
    }
}