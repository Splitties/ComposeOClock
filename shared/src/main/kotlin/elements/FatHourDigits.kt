package org.splitties.compose.oclock.sample.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.louiscad.composeoclockplayground.shared.R
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.extensions.SizeDependentState
import org.splitties.compose.oclock.sample.extensions.blurOffset
import org.splitties.compose.oclock.sample.extensions.center
import org.splitties.compose.oclock.sample.extensions.rememberGraphicsLayerAsState
import org.splitties.compose.oclock.sample.extensions.sizeForLayer

@Composable
fun FatHourDigits(
    interactiveColor: Color,
    interactiveShadowColor: Color = interactiveColor,
    ambientShadowColor: Color = interactiveShadowColor,
    interactiveShadowRepeat: Int = 1,
    ambientShadowRepeat: Int = interactiveShadowRepeat.takeIf { it > 1 } ?: 2,
    fontSize: TextUnit = 70.sp,
    interactiveBlurRadius: Dp = 10.dp,
    ambientBlurRadius: Dp = 10.dp
) {
    val interactive = fatHourDigitsLayer(
        textColor = interactiveColor,
        shadowColor = interactiveShadowColor,
        fontSize = fontSize,
        blurRadius = interactiveBlurRadius
    )
    val ambient = fatHourDigitsLayer(
        textColor = Color.Black,
        shadowColor = ambientShadowColor,
        fontSize = fontSize,
        blurRadius = ambientBlurRadius
    )
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        val count = if (isAmbient) ambientShadowRepeat else interactiveShadowRepeat
        val layer = (if (isAmbient) ambient else interactive).get()
        repeat(count) {
            drawLayer(layer)
        }
    }
}

@Composable
fun fatHourDigitsLayer(
    textColor: Color = Color.White,
    shadowColor: Color = Color.White,
    blendMode: BlendMode = BlendMode.SrcOver,
    fontSize: TextUnit = 70.sp,
    blurRadius: Dp = 10.dp
): SizeDependentState<GraphicsLayer> {
    val style = rememberFatHourDigitsTextStyle(
        textColor = textColor,
        shadowColor = shadowColor,
        blendMode = blendMode,
        fontSize = fontSize,
        blurRadius = blurRadius
    )
    val hourDigits = rememberHourDigits(style)
    return rememberGraphicsLayerAsState { layer ->
        layer.blendMode = blendMode
        layer.record(size = hourDigits.sizeForLayer()) {
            drawText(hourDigits, topLeft = hourDigits.blurOffset())
        }
        layer.center()
    }
}

@Composable
fun rememberFatHourDigitsTextStyle(
    textColor: Color = Color.White,
    shadowColor: Color = Color.White,
    blendMode: BlendMode = BlendMode.SrcOver,
    fontSize: TextUnit = 70.sp,
    drawStyle: DrawStyle = Fill,
    blurRadius: Dp = 10.dp
): TextStyle {
    val density = LocalDensity.current
    val fontFamily = remember { FontFamily(Font(R.font.outfit_extrabold)) }
    @Suppress("name_shadowing")
    val blurRadius = with(density) { blurRadius.toPx() }
    return remember(textColor, shadowColor) {
        TextStyle(
            color = textColor,
            fontSize = fontSize,
//            fontWeight = FontWeight.ExtraBold,
            drawStyle = drawStyle,
            fontFamily = fontFamily,
            shadow = if (blurRadius > 0f) Shadow(
                color = shadowColor,
                blurRadius = blurRadius
            ) else null
        )
    }
}

@Composable
fun HourDigitsUnCached(
    textColor: Color = Color.White,
    shadowColor: Color = Color.White,
    blendMode: BlendMode = BlendMode.SrcOver,
    fontSize: TextUnit = 70.sp,
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val style = remember(textColor, shadowColor) {
        val fontFamily = FontFamily(
            Font(R.font.outfit_extrabold)
        )
        TextStyle(
            color = textColor,
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = fontFamily,
            shadow = Shadow(
                color = shadowColor,
                blurRadius = with(density) { 10.dp.toPx() }
            )
        )
    }
    val time = LocalTime.current
    val measuredText = remember(time.hours) { textMeasurer.measure("${time.hours}", style) }
    OClockCanvas {
        drawText(
            measuredText,
            topLeft = center.run {
                copy(
                    x = x - measuredText.size.width / 2f,
                    y = y - measuredText.size.height / 2f
                )
            },
            blendMode = blendMode
        )
    }
}
