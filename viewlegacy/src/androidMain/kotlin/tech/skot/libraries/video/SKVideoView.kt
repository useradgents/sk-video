package tech.skot.libraries.video

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.ui.StyledPlayerView.ControllerVisibilityListener
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView
import java.util.ResourceBundle.Control

class SKVideoView(
    override val proxy: SKVideoViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: StyledPlayerView,
    player: ExoPlayer,
) : SKComponentView<StyledPlayerView>(proxy, activity, fragment, binding) {

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
            binding.setControllerVisibilityListener(object :ControllerVisibilityListener {
                override fun onVisibilityChanged(visibility: Int) {
                    it.invoke(visibility == View.VISIBLE)
                }
            })
//            binding.setControllerVisibilityListener { visibility ->
////                onControllerVisible()
//            }
        }
    }

}
