package tech.skot.libraries.video

import tech.skot.core.components.SKComponentVC
import tech.skot.core.components.SKLayoutIsSimpleView

/**
 * Un composant permettant d'afficher et de contrôler une Video
 * lue depuis une url fixe
 *
 *
 * Les méthodes onPause et onResume doivent être appelées par le ViewModel
 * sur les onPause et onResume du SKScreen contenant le composant
 *
 *
 * La vidéo est lue si le composant est "resumed" et si la propriété 'playing' est à true
 *
 * @property url chaîne fixe donnant l'url de la vidéo à lire
 * @property playing Booléen indiquant si la vidéo doit être jouée ou non
 * @property sound Booléen indiquant si le son doit être joué ou non
 * @property useCache Booléen indiquant si la vidéo doit être mise en cache ou non
 *
 */
@SKLayoutIsSimpleView
interface SKVideoVC : SKComponentVC {
    var url: String?
    val useCache: Boolean
    var playing: Boolean
    var sound: Boolean

    val onFullScreen: ((fullScreen: Boolean) -> Unit)?

    val currentPosition: Long?

    fun setCurrentPosition(position: Long)

    /**
     * méthode à appeler sur le onPause de l'écran contenant
     */
    fun onPause()

    /**
     * méthode à appeler sur le onResume de l'écran contenant
     */
    fun onResume()
}