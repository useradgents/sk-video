package tech.skot.libraries.video

import androidx.fragment.app.Fragment
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentViewProxy
import tech.skot.core.di.get

class SKVideoViewProxy(
    override val url: String,
    playingInitial: Boolean,
    soundInitial: Boolean
) : SKComponentViewProxy<PlayerView>(), SKVideoVC {


    var player: SimpleExoPlayer? = null

    private fun buildPlayer(playNow: Boolean, positionMs: Long, withSound: Boolean) =
        SimpleExoPlayer.Builder(get()).build().apply {
            seekTo(currentWindowIndex, positionMs)
            playWhenReady = playNow
            repeatMode = Player.REPEAT_MODE_ALL
            addMediaItem(MediaItem.fromUri(url))
            prepare()
            volume = if (withSound) 1f else 0f
        }


    override var sound: Boolean = soundInitial
        set(value) {
            field = value
            player?.volume = if (value) 1f else 0f
        }


    override var playing: Boolean = playingInitial
        set(value) {
            field = value
            if (value) {
                updatePlaying()
            } else {
                updatePlaying()
            }

        }

    private var savedPosition: Long = 1
    private var resumed: Boolean = false
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