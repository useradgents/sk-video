package tech.skot.libraries.video

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.ControllerVisibilityListener
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView

@UnstableApi
class SKVideoView(
    override val proxy: SKVideoViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: PlayerView,
    player: ExoPlayer,
) : SKComponentView<PlayerView>(proxy, activity, fragment, binding) {

    init {
        binding.player = player

        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                proxy.player?.release()
                proxy.player = null
                binding.player = null
            }

        })

    }

    fun setCurrentPosition(position: Long) {
        binding.player?.apply {
            seekTo(position)
            prepare()
        }

    }



    fun setOnFullScreen(onFullScreen: ((fullScreen: Boolean) -> Unit)?) {
        binding.setFullscreenButtonClickListener(onFullScreen)
    }

    fun setOnControllerVisibility(onControllerVisible:((visible: Boolean) -> Unit)?) {
        onControllerVisible?.let {
            binding.setControllerVisibilityListener(object : ControllerVisibilityListener {
                override fun onVisibilityChanged(visibility: Int) {
                    it.invoke(visibility == View.VISIBLE)
                }
            })
        }
    }

}