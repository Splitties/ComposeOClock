package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.ComposeOClockWatermark
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.WearPreviewSizesProvider
import org.splitties.compose.oclock.sample.elements.clockHand
import org.splitties.compose.oclock.sample.elements.setToHeart
import org.splitties.compose.oclock.sample.elements.setToKotlinLogo
import org.splitties.compose.oclock.sample.extensions.centerAsTopLeft
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize
import org.splitties.compose.oclock.sample.extensions.rotate

@Composable
fun KotlinFanClock() {
    Background()
    ComposeOClockWatermark(niceBrush)
    KotlinLogoHourPips()
    HoursHand()
    MinutesHand()
    SecondsHand()
    CenterDot()
}

private val niceBrush = Brush.sweepGradient(
    4.5f / 8f to Color(0x7C00E5FF),
    5.1f / 8f to Color(0xFF00E5FF),
    6.2f / 8f to Color(0xFF00E5FF),
    6.5f / 8f to Color(0xFFFFFF8D),
)

@Composable
private fun Background() {
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        if (isAmbient.not()) drawCircle(kotlinDarkBg)
    }
}

@Composable
private fun HoursHand() {
    val time = LocalTime.current
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        rotate(degrees = time.hourWithMinutes * 30) {
            val width = 16.dp.toPx()
            clockHand(
                brush = SolidColor(kotlinLogoColors[0]),
                width = width,
                height = size.height / 4f,
                style = if (isAmbient) Stroke(width = 3.dp.toPx()) else Fill,
                blendMode = BlendMode.Plus
            )
        }
    }
}

@Composable
private fun MinutesHand() {
    val time = LocalTime.current
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        rotate(degrees = time.minutes * 6f) {
            val width = 10.dp.toPx()
            clockHand(
                brush = SolidColor(kotlinLogoColors[1]),
                width = width,
                height = size.height * 3 / 8f,
                style = if (isAmbient) Stroke(width = 3.dp.toPx()) else Fill,
                blendMode = BlendMode.Plus
            )
        }
    }
}

@Composable
private fun SecondsHand() {
    val time = LocalTime.current
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        if (isAmbient) return@OClockCanvas
        val color = kotlinLogoColors[2]
        drawLine(
            color,
            start = center,
            end = center.copy(y = size.height / 32f).rotate(time.seconds * 6f),
            strokeWidth = 2.dp.toPx(),
            blendMode = BlendMode.Lighten,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun CenterDot() {
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        drawCircle(kotlinDarkBg, radius = 6.dp.toPx(), blendMode = BlendMode.Xor)
        drawCircle(
            color = if (isAmbient) Color.Black else kotlinLogoColors[2],
            radius = 5.dp.toPx()
        )
        if (isAmbient) {
            drawCircle(
                Color.Gray,
                radius = 5.dp.toPx()
            )
            drawCircle(
                Color.Black,
                radius = 2.dp.toPx()
            )
        }
    }
}

@Composable
private fun KotlinLogoHourPips() {
    val filledKotlinLogoPath = remember { Path() }
    val outlinedKotlinLogoPath = remember { Path() }
    val heartPath = remember { Path() }
    val edgePadding = 28.dp
    val cachedStroke = rememberStateWithSize {
        Stroke(
            1.5f.dp.toPx(),
            cap = StrokeCap.Butt,
            join = StrokeJoin.Miter
        )
        Stroke(1.5f.dp.toPx(), cap = StrokeCap.Butt, join = StrokeJoin.Miter)
    }
    val cachedPipsGradient = rememberStateWithSize {
        val side = size.minDimension / 12f
        val topLeft = Offset(x = center.x, y = side / 2f + edgePadding.toPx()).centerAsTopLeft(side)
        filledKotlinLogoPath.setToKotlinLogo(side, topLeft)
        outlinedKotlinLogoPath.setToKotlinLogo(
            side = side,
            topLeft = topLeft,
            stroke = cachedStroke.get()
        )
        val heartSize = Size(side, side)
        heartPath.setToHeart(topLeft, heartSize)
        Brush.linearGradient(
            kotlinLogoColors,
            start = topLeft.run { copy(y = y + side) },
            end = topLeft.run { copy(x = x + side) }
        )
    }
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        val stroke by cachedStroke
        val pipsGradient by cachedPipsGradient
        val kotlinLogoPath: Path
        val style: DrawStyle
        if (isAmbient) {
            style = stroke
            kotlinLogoPath = outlinedKotlinLogoPath
        } else {
            style = Fill
            kotlinLogoPath = filledKotlinLogoPath
        }
        val count = 12
        repeat(count) {
            rotate(360f * it / count) {
                if (it % 3 == 0) {
                    if (isAmbient) {
                        drawPath(kotlinLogoPath, color = kotlinBlue, style = style)
                    } else {
                        drawPath(kotlinLogoPath, brush = pipsGradient, style = style)
                    }
                } else {
                    if (isAmbient) {
                        drawPath(heartPath, brush = pipsGradient, style = style)
                    } else {
                        drawPath(heartPath, color = kotlinBlue, style = style)
                    }
                }
            }
        }
    }
}

private val kotlinDarkBg = Color(0xFF1B1B1B)
private val kotlinBlue = Color(0xFF7F52FF)
private val kotlinLogoColors = listOf(
    kotlinBlue,
    Color(0xFF_C811E2),
    Color(0xFF_E54857),
)

@WatchFacePreview
@Composable
private fun KotlinFanClockPreview(
    @PreviewParameter(WearPreviewSizesProvider::class) size: Dp
) = WatchFacePreview(size) {
    KotlinFanClock()
}
