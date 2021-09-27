//[viewmodel](../../../index.md)/[tech.skot.libraries.video](../index.md)/[SKVideo](index.md)

# SKVideo

[common]\
class [SKVideo](index.md)(url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), playingInitial: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), soundInitial: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : SKComponent&lt;[SKVideoVC](../../../../viewcontract/viewcontract/tech.skot.libraries.video/-s-k-video-v-c/index.md)&gt; 

A Video Component Based on ExoPlayer for Android no iOs Version at this time

## Parameters

common

| | |
|---|---|
| url | url of video you want play |
| playingInitial | indicate if the video should play automatically when ready |
| soundInitial | indicate if the soud should be on or of |

## Constructors

| | |
|---|---|
| [SKVideo](-s-k-video.md) | [common]<br>fun [SKVideo](-s-k-video.md)(url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), playingInitial: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, soundInitial: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) |

## Functions

| Name | Summary |
|---|---|
| [onPause](on-pause.md) | [common]<br>fun [onPause](on-pause.md)() |
| [onResume](on-resume.md) | [common]<br>fun [onResume](on-resume.md)() |

## Properties

| Name | Summary |
|---|---|
| [playing](playing.md) | [common]<br>var [playing](playing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>indicate if the video should play, play if true, pause if false |
| [sound](sound.md) | [common]<br>var [sound](sound.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [view](view.md) | [common]<br>open override val [view](view.md): [SKVideoVC](../../../../viewcontract/viewcontract/tech.skot.libraries.video/-s-k-video-v-c/index.md) |
