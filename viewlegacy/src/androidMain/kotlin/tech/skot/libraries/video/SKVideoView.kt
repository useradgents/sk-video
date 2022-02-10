package tech.skot.libraries.video

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView

class SKVideoView(
    override val proxy:SKVideoViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: PlayerView,
    player: ExoPlayer
) : SKComponentView<PlayerView>(proxy, activity, fragment, binding) {

    init {
        binding.player = player

        lifecycle.addObserver(object:DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                proxy.player?.release()
                proxy.player = null
                binding.player = null
            }
        })

    }

}
