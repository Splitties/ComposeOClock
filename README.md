# Compose O'Clock

Draw Wear OS Watch Faces with Compose Canvas (& runtime)

Compose O'Clock is the fastest way to develop a good looking and efficient Watch Face for Wear OS.

Using this SDK, knowing how to use Jetpack Compose is about 96% of the knowledge you need to know to
successfully get a proper watch face that you can also see live in a phone app.

The 4% remaining to develop a good watch face app is documented here.

## Features

- ✅ **Live-preview** your Watch Face in Android Studio, a phone, or a watch
- ✅ **Adaptive refresh-rate** out of the box (up to 60fps on watches, and 120fps on phones)
- ✅ Compose Canvas and text APIs (**Kotlin-friendly!**)
- ✅ Shader effects (Wear OS 4+)
- ✅ Auto recomposition or re-render on state change
- ✅ Convenience **time APIs** for clocks
- ✅ Watch Face **specific optimizations**
- ✅ **Instant** style changes, including while in ambient mode
- ✅ Render your Watch Faces **in any Composable** in the phone or watch app
- ✅ Full freedom for **complications** rendering (custom data like weather, step counts, heart rate…)
- ✅ **Technical support** available for pro licenses

## Getting started

This repository contains a starter project.
After cloning the project and opening it in Android Studio, you can navigate to the Watch Face
entry point: [SampleWatchFaceService.kt](app-watch/src/main/kotlin/SampleWatchFaceService.kt).

It all starts with this function inside `ComposeWatchFaceService`:

```kotlin
@Composable
override fun Content(complicationData: Map<Int, StateFlow<ComplicationData>>) {
    MyWatchFace()
}
```

Here's how a simple, typical Watch Face looks like:

```kotlin
@Composable
fun MyWatchFace() {
    FancyBackground()
    CoolHourPips()
    SomeExtraStuff()
    HourHand()
    MinutesHand()
    SecondsHand()
}
```

Inside those other composables, you need to use `OClockCanvas` at some point to draw things.

It's exactly like `Canvas` from Compose, except that it also takes an optional `onTap` parameter,
and that it will work in a Watch Face.

Here's how `SecondsHand()` from above could look like:

```kotlin
@Composable
private fun SecondsHand() {
    val time = LocalTime.current
    val isAmbient by LocalIsAmbient.current // We use delegation to get a Boolean here
    OClockCanvas {
        if (isAmbient) return@OClockCanvas // Don't read seconds in ambient mode, return early.
        // Because we read the time just below,
        // this will auto-re-render when the second changes.
        val degrees = time.seconds * 6f // 60s × 6 -> 360°
        val top = size.height / 32f
        drawLine( // Can use anything from Compose DrawScope
            someColor,
            start = center.copy(y = top).rotate(degrees),
            end = center,
            strokeWidth = 2.dp.toPx(),
            blendMode = BlendMode.Lighten, // Because why not! More on that below.
            cap = StrokeCap.Round
        )
    }
}
```

## `OClockCanvas`

### One canvas for all by default

`OClockCanvas` is actually a bit more powerful than `Canvas`:
All `OClockCanvas` will share the same underlying canvas, without layering by default,
which means that if you use a `BlendMode`, it will apply over everything that has been drawn yet.

It can have multiple use cases, like:
- Tinting what's been drawn before (without having to touch those components), using `BlendMode.SrcIn`
- Drawing on the pixels that haven't been drawn yet (e.g. draw a background last), using `BlendMode.DstOver`
- Avoid overlaps, using `BlendMode.Xor` for example.
- Make overlaps visible, using `BlendMode.Plus` or `BlendMode.Lighten` for example.

### Taps

Wear OS Watch Faces support taps (but not swipes, which the system already uses for tiles, notifications, and quick settings).

When displaying an `OClockCanvas` element in a regular compose hierarchy (on the phone, in a watch, or in Android Studio preview),
the behavior will be exactly the same as when set as a Watch Face: a swipe will cause the tap to be cancelled. Proper taps will be registered.

Make sure your `onTap` handlers return `true` if the tap occurs in the region where you support actions, and `false` otherwise,
**regardless of whether it's a tap down or up**.

Also make sure tap actions are fired only on tap-up, and make sure you're not leaving broken/unwanted state on tap cancel.

In addition to the `event`, the `onTap` lambda has an `OnTapScope` receiver, which contains the `size`, `center`, and density info, so it can be used along with the `position` `Offset` from the event to determine whether the tap should be handled or not.

## Complications

The complications APIs from `androidx.wear.watchface` are… well… complicated.
Compose O'Clock provides several Compose friendly extensions that cover all types of complications
that Wear OS supports.

_Complication related code won't crash when running on a phone. Showing placeholder data is supported,
and it's possible to forward live the actual complication data from the watch face to the phone app._

### For `ComplicationText?` & `ComplicationText`

```kotlin
@Composable
fun ComplicationText?.rememberMeasuredAsState(
    default: String,
    callMeasure: TextMeasurer.(string: String) -> TextLayoutResult
): State<TextLayoutResult>

@Composable
fun ComplicationText.rememberMeasuredAsState(
    callMeasure: TextMeasurer.(string: String) -> TextLayoutResult
): State<TextLayoutResult>
```

Note that `TextLayoutResult` can directly be used in `drawText` inside `OClockCanvas`.

### For images/icons in `ComplicationData` subclasses

```kotlin
@Composable
inline fun NoPermissionComplicationData.rememberDrawableAsState(
    preferSmallImage: Boolean = false,
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode
): State<Drawable?>

@Composable
inline fun LongTextComplicationData.rememberDrawableAsState(
    preferSmallImage: Boolean = false,
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode
): State<Drawable?>

@Composable
inline fun MonochromaticImageComplicationData.rememberDrawableAsState(
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode
): State<Drawable?>

@Composable
inline fun PhotoImageComplicationData.rememberDrawableAsState(
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
): State<Drawable?>

@Composable
inline fun RangedValueComplicationData.rememberDrawableAsState(
    preferSmallImage: Boolean = false,
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode
): State<Drawable?>

@Composable
inline fun ShortTextComplicationData.rememberDrawableAsState(
    preferSmallImage: Boolean = false,
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode
): State<Drawable?>

@Composable
inline fun SmallImageComplicationData.rememberDrawableAsState(
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode
): State<Drawable?>

@RequiresApi(33)
@Composable
inline fun GoalProgressComplicationData.rememberDrawableAsState(
    preferSmallImage: Boolean = false,
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode
): State<Drawable?>

@RequiresApi(33)
@Composable
inline fun WeightedElementsComplicationData.rememberDrawableAsState(
    preferSmallImage: Boolean = false,
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode
): State<Drawable?>
```

### Other complications related convenience extensions for advanced use cases

```kotlin
fun Drawable.setTintBlendMode(blendMode: BlendMode) // BlendMode from Compose UI graphics
fun Drawable.setTint(color: Color) // Color from Compose UI graphics

@Composable
fun SmallImage.rememberDrawableAsState(
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode,
): State<Drawable?>

@Composable
fun MonochromaticImage.rememberDrawableAsState(
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode,
): State<Drawable?>

@Composable
fun rememberDrawableAsState(
    monochromaticImage: MonochromaticImage?,
    smallImage: SmallImage?,
    preferSmallImage: Boolean = false,
    tint: Color = Color.Unspecified,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    ambientTint: Color = tint,
    ambientTintBlendMode: BlendMode = tintBlendMode,
): State<Drawable?>
```

## License

Compose O'Clock has a dual license:

It's free to use in debug apps, and you are free to use the dependency in any project that is compatible with the Apache 2.0 license.

However, releasing an app using Compose O'Clock requires a commercial license.

If you want to release a Watch Face app for Wear OS that uses Compose O'Clock, please send an email to [composeoclock@splitties.org](mailto:composeoclock@splitties.org)

## Why Compose O'Clock?

Currently, there are 3 ways to make a Watch Face for Wear OS:

1. `androidx.wear.watchface`
2. Samsung Watch Face Studio (WFS)
3. XML based Watch Face Format (WFF)

`androidx.wear.watchface` gives a lot of flexibility, but the APIs require a lot of boilerplate,
which can steer you away from finishing and refining your watch face idea.
Also, you get no IDE preview.

Compose O'Clock uses `androidx.wear.watchface` internally, to ensure the best compatibility.
However, the developer/designer experience is much improved because the API is simplified,
many optimizations require much fewer code, or no code, and you get super fast live-preview in
Android Studio, or on a phone app. You get both WYSIWYG (What You See is What You Get),
and the power of Kotlin code.

Samsung Watch Face Studio (WFS) is a WYSIWYG editor.
Features are limited, making it likely your Watch Face will look like yet another basic one.
Samsung WFS outputs XML based Watch Face Format (WFF), so all WFF limitations apply to WFS.

XML based Watch Face Format (WFF) has many limitations. Here's a non exhaustive list:
- Works only on Wear OS 4: many non Samsung and non Google watches are still on Wear OS 2 or 3. 🚫
- Capped at 15fps: sensor based animations, and other animations never look smooth. 🐌
- No live-preview: you need to re-build and re-deploy each time you want to see your changes ⏳
- Settings are very limited (numbers and gradients are not supported, unless you hack deeply) 
- You can't control the order in which settings will appear 🤷🏻🤪
- No support for in-app purchases(!) 💸
- No usage statistics possible
- Near-zero connection with your users
- No interactivity apart from switching raster images, and launching complications 🔒
- Doesn't support all types of complications
- No runtime logic except for basic arithmetic expressions
- No support for shaders
- No support for blend modes (except masking)
- On phone editor is slow and hard to discover 🐌
- As of Wear OS 4, the WFF renderer is no better than what's possible with `androidx.wear.watchface`
- Publishing on the Play Store is as long as any other app, if not more, despite reduced risks

If, for some reason, you still want to try WFF, without the inconvenience of writing XML by hand,
we made a Kotlin DSL based on kotlinx.html: https://github.com/Splitties/WffDsl
We decided to not publish it because the dev UX and product potential isn't great at the moment.

We are waiting for the Wear OS team at Google to make an on-device API, so executable watch faces,
like ones made with Compose O'Clock, or `androidx.wear.watchface`, can update the WFF that
will be used for ambient mode or energy saver mode (which will make sense once the WFF renderer is improved).

## Credits

Thanks to [Yuri Schimke](https://github.com/yschimke) for encouraging me to try making a Compose API for Watch Faces, and for all the helpful input! 🙏❤️
