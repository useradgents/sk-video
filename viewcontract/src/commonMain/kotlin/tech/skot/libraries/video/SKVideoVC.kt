package tech.skot.libraries.video

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutIsSimpleView

/**
 * Un composant permettant d'afficher et de contrôler une Video
 * lue depuis une url fixe
 *
 *
 * Les méthodes onPause et onResume doivent être appelées par le ViewModel lors
 * sur les onPause et onResume du SKScreen contenant le composant
 *
 * 
 * La vidéo est lue si le composant est "resumed" et si la propriété 'playing' est à true
 *
 * @property url chaîne fixe donnant l'url de la vidéo à lire
 * @property playing Booléen indiquant si la vidéo doit être jouée ou non
 * @property playing Booléen indiquant si le son doit être joué ou non
 *
 *
 *
 */
@SKLayoutIsSimpleView
interface SKVideoVC: SKComponentVC {
    val url:String
    var playing:Boolean
    var sound:Boolean

    /**
     * méthode à appelé sur le onPause de l'écran contenant
     */
    fun onPause()

    /**
     * méthode à appelé sur le onResume de l'écran contenant
     */
    fun onResume()
}