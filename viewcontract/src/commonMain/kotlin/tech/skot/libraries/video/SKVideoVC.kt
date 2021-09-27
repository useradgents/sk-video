package tech.skot.libraries.video

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutIsSimpleView

@SKLayoutIsSimpleView
interface SKVideoVC: SKComponentVC {
    val url:String
    var playing:Boolean
    var sound:Boolean

    fun onPause()
    fun onResume()
}