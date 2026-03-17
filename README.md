# SKVideo
un composant Skot pour afficher/contrôler une vidéo
Pour l'instant seule la version android "legacy" est disponible

Elle est basée sur ExoPlayer

## Utilisation

Pour utiliser la librairie vous devez ajouter la ligne suivante à votre fichier _skot_librairies.properties_

`com.github.skot-framework.sk-video:0.0.1_1.0.0-alpha55,tech.skot.libraries.video.di.skvideoModule`

Cette librairie consiste en un seul SKComponent:


son ViewContract : [SKVideoVC](/docs/gfm/viewcontract/viewcontract/tech.skot.libraries.video/-s-k-video-v-c/index.md) 

son ViewModel (open) : [SKVideoVM](/docs/gfm/viewmodel/viewmodel/tech.skot.libraries.video/-s-k-video/index.md) 


Attention son comportement est le suivant :

il faut appeler les méthodes onPause et onResume sur les onPause et onResume du SKScreen parent du composant.

La lecture de la vidéo dépend à la fois d'une propriété `playing` et du fait que l'écran soit resumed ou non


[documentation](docs/gfm/index.md)



