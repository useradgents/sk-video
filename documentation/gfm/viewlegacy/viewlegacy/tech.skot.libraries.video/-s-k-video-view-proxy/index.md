//[viewlegacy](../../../index.md)/[tech.skot.libraries.video](../index.md)/[SKVideoViewProxy](index.md)

# SKVideoViewProxy

[android]\
class [SKVideoViewProxy](index.md)(url: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), playingInitial: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), soundInitial: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : SKComponentViewProxy&lt;PlayerView&gt; , [SKVideoVC](../../../../viewcontract/viewcontract/tech.skot.libraries.video/-s-k-video-v-c/index.md)

## Functions

| Name | Summary |
|---|---|
| [_bindTo](index.md#1965070256%2FFunctions%2F-2118544462) | [android]<br>fun [_bindTo](index.md#1965070256%2FFunctions%2F-2118544462)(activity: SKActivity, fragment: [Fragment](https://developer.android.com/reference/kotlin/androidx/fragment/app/Fragment.html)?, binding: PlayerView, collectingObservers: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): SKComponentView&lt;PlayerView&gt; |
| [bindingOf](index.md#-218721014%2FFunctions%2F-2118544462) | [android]<br>open fun [bindingOf](index.md#-218721014%2FFunctions%2F-2118544462)(view: [View](https://developer.android.com/reference/kotlin/android/view/View.html)): PlayerView |
| [bindTo](bind-to.md) | [android]<br>open override fun [bindTo](bind-to.md)(activity: SKActivity, fragment: [Fragment](https://developer.android.com/reference/kotlin/androidx/fragment/app/Fragment.html)?, binding: PlayerView, collectingObservers: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [SKVideoView](../-s-k-video-view/index.md) |
| [bindToItemView](index.md#1853397514%2FFunctions%2F-2118544462) | [android]<br>fun [bindToItemView](index.md#1853397514%2FFunctions%2F-2118544462)(activity: SKActivity, fragment: [Fragment](https://developer.android.com/reference/kotlin/androidx/fragment/app/Fragment.html)?, view: [View](https://developer.android.com/reference/kotlin/android/view/View.html)): SKComponentView&lt;PlayerView&gt; |
| [bindToView](index.md#-1886702137%2FFunctions%2F-2118544462) | [android]<br>fun [bindToView](index.md#-1886702137%2FFunctions%2F-2118544462)(activity: SKActivity, fragment: [Fragment](https://developer.android.com/reference/kotlin/androidx/fragment/app/Fragment.html)?, view: [View](https://developer.android.com/reference/kotlin/android/view/View.html), collectingObservers: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): SKComponentView&lt;PlayerView&gt; |
| [closeKeyboard](index.md#-1620891610%2FFunctions%2F-2118544462) | [android]<br>open override fun [closeKeyboard](index.md#-1620891610%2FFunctions%2F-2118544462)() |
| [displayErrorMessage](index.md#491242464%2FFunctions%2F-2118544462) | [android]<br>open override fun [displayErrorMessage](index.md#491242464%2FFunctions%2F-2118544462)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [inflate](index.md#-1312652885%2FFunctions%2F-2118544462) | [android]<br>open fun [inflate](index.md#-1312652885%2FFunctions%2F-2118544462)(layoutInflater: [LayoutInflater](https://developer.android.com/reference/kotlin/android/view/LayoutInflater.html), parent: [ViewGroup](https://developer.android.com/reference/kotlin/android/view/ViewGroup.html)?, attachToParent: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): PlayerView |
| [inflateInParentAndBind](index.md#1955542503%2FFunctions%2F-2118544462) | [android]<br>fun [inflateInParentAndBind](index.md#1955542503%2FFunctions%2F-2118544462)(activity: SKActivity, fragment: [Fragment](https://developer.android.com/reference/kotlin/androidx/fragment/app/Fragment.html)?, parent: [ViewGroup](https://developer.android.com/reference/kotlin/android/view/ViewGroup.html)) |
| [onPause](on-pause.md) | [android]<br>open override fun [onPause](on-pause.md)() |
| [onRemove](on-remove.md) | [android]<br>open override fun [onRemove](on-remove.md)() |
| [onResume](on-resume.md) | [android]<br>open override fun [onResume](on-resume.md)() |
| [saveState](index.md#1131160241%2FFunctions%2F-2118544462) | [android]<br>open fun [saveState](index.md#1131160241%2FFunctions%2F-2118544462)() |
| [updatePlaying](update-playing.md) | [android]<br>fun [updatePlaying](update-playing.md)() |

## Properties

| Name | Summary |
|---|---|
| [layoutId](index.md#-1846570256%2FProperties%2F-2118544462) | [android]<br>open val [layoutId](index.md#-1846570256%2FProperties%2F-2118544462): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)? |
| [player](player.md) | [android]<br>var [player](player.md): SimpleExoPlayer? = null |
| [playing](playing.md) | [android]<br>open override var [playing](playing.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [sound](sound.md) | [android]<br>open override var [sound](sound.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [url](url.md) | [android]<br>open override val [url](url.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
