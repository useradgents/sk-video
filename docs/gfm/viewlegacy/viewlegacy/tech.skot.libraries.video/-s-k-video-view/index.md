//[viewlegacy](../../../index.md)/[tech.skot.libraries.video](../index.md)/[SKVideoView](index.md)

# SKVideoView

[android]\
class [SKVideoView](index.md)(proxy: [SKVideoViewProxy](../-s-k-video-view-proxy/index.md), activity: SKActivity, fragment: [Fragment](https://developer.android.com/reference/kotlin/androidx/fragment/app/Fragment.html)?, binding: PlayerView, player: SimpleExoPlayer) : SKComponentView&lt;PlayerView&gt;

## Functions

| Name | Summary |
|---|---|
| [closeKeyboard](index.md#1428997254%2FFunctions%2F-2118544462) | [android]<br>fun [closeKeyboard](index.md#1428997254%2FFunctions%2F-2118544462)() |
| [displayError](index.md#-1092193227%2FFunctions%2F-2118544462) | [android]<br>fun [displayError](index.md#-1092193227%2FFunctions%2F-2118544462)(message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [getLifecycle](index.md#-2126188895%2FFunctions%2F-2118544462) | [android]<br>open override fun [getLifecycle](index.md#-2126188895%2FFunctions%2F-2118544462)(): [Lifecycle](https://developer.android.com/reference/kotlin/androidx/lifecycle/Lifecycle.html) |
| [observe](index.md#665239928%2FFunctions%2F-2118544462) | [android]<br>fun &lt;[D](index.md#665239928%2FFunctions%2F-2118544462)&gt; SKLiveData&lt;[D](index.md#665239928%2FFunctions%2F-2118544462)&gt;.[observe](index.md#665239928%2FFunctions%2F-2118544462)(onChanged: ([D](index.md#665239928%2FFunctions%2F-2118544462)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))<br>fun &lt;[D](index.md#2043339295%2FFunctions%2F-2118544462)&gt; SKMessage&lt;[D](index.md#2043339295%2FFunctions%2F-2118544462)&gt;.[observe](index.md#2043339295%2FFunctions%2F-2118544462)(onReceive: ([D](index.md#2043339295%2FFunctions%2F-2118544462)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [removeObservers](index.md#810891660%2FFunctions%2F-2118544462) | [android]<br>open fun [removeObservers](index.md#810891660%2FFunctions%2F-2118544462)() |
| [setTextColor](index.md#1463877402%2FFunctions%2F-2118544462) | [android]<br>fun [TextView](https://developer.android.com/reference/kotlin/android/widget/TextView.html).[setTextColor](index.md#1463877402%2FFunctions%2F-2118544462)(color: Color) |

## Properties

| Name | Summary |
|---|---|
| [activity](index.md#-63476570%2FProperties%2F-2118544462) | [android]<br>val [activity](index.md#-63476570%2FProperties%2F-2118544462): SKActivity |
| [binding](index.md#-75864896%2FProperties%2F-2118544462) | [android]<br>val [binding](index.md#-75864896%2FProperties%2F-2118544462): PlayerView |
| [collectObservers](index.md#-520305182%2FProperties%2F-2118544462) | [android]<br>var [collectObservers](index.md#-520305182%2FProperties%2F-2118544462): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [context](index.md#671965398%2FProperties%2F-2118544462) | [android]<br>val [context](index.md#671965398%2FProperties%2F-2118544462): [Context](https://developer.android.com/reference/kotlin/android/content/Context.html) |
| [proxy](proxy.md) | [android]<br>open override val [proxy](proxy.md): [SKVideoViewProxy](../-s-k-video-view-proxy/index.md) |
