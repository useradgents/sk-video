package tech.skot.libraries.video

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import tech.skot.core.components.SKActivity
import tech.skot.core.components.SKComponentView

class SKVideoView(
    override val proxy:SKVideoViewProxy,
    activity: SKActivity,
    fragment: Fragment?,
    binding: PlayerView,
    player: SimpleExoPlayer
) : SKComponentView<PlayerView>(proxy, activity, fragment, binding) {

    init {
        binding.player = player

        lifecycle.addObserver(object: LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                proxy.player?.release()
                proxy.player = null
                binding.player = null
            }

        })

    }

}
