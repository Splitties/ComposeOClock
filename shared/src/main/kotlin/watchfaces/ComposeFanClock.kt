package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.louiscad.composeoclockplayground.shared.R
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.WearPreviewSizesProvider
import org.splitties.compose.oclock.sample.elements.SinusoidalCrown
import org.splitties.compose.oclock.sample.elements.clockHand
import org.splitties.compose.oclock.sample.elements.setToHeart
import org.splitties.compose.oclock.sample.elements.vectors.rememberComposeMultiplatformVectorPainter
import org.splitties.compose.oclock.sample.extensions.centerAsTopLeft
import org.splitties.compose.oclock.sample.extensions.drawPainter
import org.splitties.compose.oclock.sample.extensions.fitIn
import org.splitties.compose.oclock.sample.extensions.loop
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize
import org.splitties.compose.oclock.sample.extensions.rotate

@Composable
fun ComposeFanClock() {
    Background()
    HeartHourPips()
    BigComposeLogoBg()
    HoursHand()
    MinutesHand()
    val time = LocalTime.current
    val isAmbient by LocalIsAmbient.current
    SinusoidalCrown(
        getRatio = {
            if (isAmbient) 0f else time.secondsWithMillis / 60f
        },
        interactiveFillBrush = remember { Brush.sweepGradient(someColors) },
        ambientFillBrush = remember { Brush.sweepGradient(brightColors) }
    )
    SecondsHand()
}

private val brightColors = listOf(
    Color(0x7C00E5FF),
    Color(0xFF00E5FF),
    Color(0xFF64DD17),
)

private val someColors = listOf(
    Color(0xFF3D5AFE),
    Color(0xFF00E5FF),
    Color(0xFF00B0FF),
).loop()

@Composable
private fun BigComposeLogoBg() {
    val isAmbient by LocalIsAmbient.current
    val composeMultiplatformLogo = rememberComposeMultiplatformVectorPainter()
    val composeLogo = painterResource(R.drawable.jetpack_compose)
    val ambientProgress by animateFloatAsState(
        if (isAmbient) 1f else 0f
    )
    OClockCanvas {
        val logo = if (isAmbient) composeMultiplatformLogo else composeLogo
        val s = logo.intrinsicSize.fitIn(size / 2.5f)
        if (ambientProgress != 0f) {
            drawPainter(
                painter = composeMultiplatformLogo, topLeft = center.centerAsTopLeft(s),
                size = s,
                alpha = ambientProgress
            )
        }
        if (ambientProgress != 1f) {
            drawPainter(
                painter = composeLogo, topLeft = center.centerAsTopLeft(s),
                size = s,
                alpha = 1f - ambientProgress
            )
        }
    }
}

private val ambientTransitionSpec = spring<Float>(stiffness = Spring.StiffnessLow)

private val composeMultiplatformColor = Color(red = 66, green = 133, blue = 244)

@Composable
private fun Background() {
    val isAmbient by LocalIsAmbient.current
    val ambientColor = Color.Black
    val interactiveColor = Color.White
    val ambientProgress by animateFloatAsState(
        targetValue = if (isAmbient) 1f else 0f,
        animationSpec = tween(durationMillis = 500)
    )
    OClockCanvas {
        val showAmbientBg = ambientProgress != 0f
        val showInteractiveBg = ambientProgress != 1f
        if (showAmbientBg) {
            drawCircle(ambientColor)
        }
        if (showInteractiveBg) {
            val radius = (size.minDimension / 2f) * (1 - ambientProgress)
            drawCircle(interactiveColor, radius = radius)
        }
    }
}

@Composable
private fun HoursHand() {
    val time = LocalTime.current
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        rotate(degrees = time.hourWithMinutes * 30) {
            val width = 10.dp.toPx()
            clockHand(
                brush = SolidColor(someColors[2].copy(alpha = 1f)),
                width = width,
                height = size.height / 4f,
                style = if (isAmbient) Stroke(width = 3.dp.toPx()) else Fill,
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
                brush = SolidColor(someColors[1].copy(alpha = 1f)),
                width = width,
                height = size.height * 4 / 10f,
                style = if (isAmbient) Stroke(width = 3.dp.toPx()) else Fill,
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
        val color = brightColors[2]
        drawLine(
            color,
            start = center,
            end = center.copy(y = size.height / 32f).rotate(time.seconds * 6f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

private fun brushForHearts(
    baseColor: Color,
    start: Offset,
    end: Offset
): Brush = Brush.linearGradient(
    colors = listOf(
        baseColor.run { copy(alpha = alpha * .7f) },
        baseColor.run { copy(alpha = alpha * .3f) }
    ),
    start = start,
    end = end
)

@Composable
private fun HeartHourPips() {
    val heartPath = remember { Path() }
    val cachedStroke = rememberStateWithSize {
        Stroke(
            1.5f.dp.toPx(),
            cap = StrokeCap.Butt,
            join = StrokeJoin.Miter
        )
        Stroke(1.5f.dp.toPx(), cap = StrokeCap.Butt, join = StrokeJoin.Miter)
    }
    val isAmbient by LocalIsAmbient.current
    val cachedBrushes = rememberStateWithSize {
        val padding = size.minDimension / (7f)
        val side = size.minDimension / 8f
        val topLeft = Offset(x = center.x, y = side / 2f + padding).centerAsTopLeft(side)
        val heartSize = Size(side, side)
        heartPath.setToHeart(topLeft, heartSize)
        val start = topLeft.run { copy(y = y + side) }
        val end = topLeft.run { copy(x = x + side) }
        val interactiveGradient = brushForHearts(Color(0x56949494), start, end)
        val ambientGradient = brushForHearts(Color(0xFF81D4FA), start, end)
        interactiveGradient to ambientGradient
    }
    val fillAlpha by animateFloatAsState(
        targetValue = if (isAmbient) 0.3f else 1f,
        animationSpec = ambientTransitionSpec,
        label = "minor-hour-pips- fill-alpha"
    )
    OClockCanvas {
        val stroke by cachedStroke
        val (interactiveBrush, ambientBrush) = cachedBrushes.get()
        val brush = if (isAmbient) ambientBrush else interactiveBrush
        val count = 12
        repeat(count) {
            rotate(360f * it / count) {
                drawPath(heartPath, brush = brush, style = stroke)
                if (fillAlpha != 0f) {
                    drawPath(
                        heartPath,
                        brush = brush,
                        alpha = fillAlpha
                    )
                }
            }
        }
    }
}

@WatchFacePreview
@Composable
private fun ComposeFanClockPreview(
    @PreviewParameter(WearPreviewSizesProvider::class) size: Dp
) = WatchFacePreview(size) {
    ComposeFanClock()
}
