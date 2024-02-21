package org.splitties.compose.oclock.sample.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.WearPreviewSizesProvider
import org.splitties.compose.oclock.sample.elements.vectors.rememberComposeMultiplatformVectorPainter
import org.splitties.compose.oclock.sample.elements.vectors.rememberWearOsLogoVectorPainter
import org.splitties.compose.oclock.sample.extensions.centerAsTopLeft
import org.splitties.compose.oclock.sample.extensions.drawPainter
import org.splitties.compose.oclock.sample.extensions.moveTo
import org.splitties.compose.oclock.sample.extensions.quadraticBezierTo
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize
import org.splitties.compose.oclock.sample.extensions.rotateAround

@WatchFacePreview
@Composable
fun SinusoidalCrownPreview(
    @PreviewParameter(WearPreviewSizesProvider::class) size: Dp
) = WatchFacePreview(size) {
    val time = LocalTime.current
    SinusoidalCrown(getRatio = { time.minutesWithSeconds / 60f })
    val wearOsLogo = rememberWearOsLogoVectorPainter()
    val composeLogo = rememberComposeMultiplatformVectorPainter()
    val washingOutFilter = remember {
        ColorFilter.lighting(
            add = Color.Gray,
            multiply = Color.hsl(hue = 0f, saturation = 0f, lightness = .2f)
        )
    }
    OClockCanvas {
        scale(.2f) {
            drawPainter(
                wearOsLogo,
                topLeft = center.centerAsTopLeft(wearOsLogo.intrinsicSize),
                colorFilter = washingOutFilter,
                alpha = .5f
            )
        }
        drawPainter(
            painter = composeLogo,
            topLeft = center.centerAsTopLeft(composeLogo.intrinsicSize)
        )
    }
}

@Composable
fun SinusoidalCrown(
    getRatio: () -> Float,
    interactiveFillBrush: Brush = remember { SolidColor(Color.Cyan) },
    ambientFillBrush: Brush = remember { SolidColor(Color.Cyan) }
) {
    val pathMeasure = remember { PathMeasure() }
    val cachedPath = remember { Path() }.let { path ->
        rememberStateWithSize {
            path.also {
                it.setToSineWaveLike(size)
                pathMeasure.setPath(it, forceClosed = false)
            }
        }
    }
    val wholePathStroke = rememberStateWithSize {
        Stroke(width = 1.dp.toPx())
    }
    val progressPathStroke = rememberStateWithSize {
        Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
    }
    val segment = remember { Path() }
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        val path by cachedPath
        val ratio = getRatio()
        segment.reset()
        pathMeasure.getSegment(
            startDistance = 0f,
            stopDistance = ratio * pathMeasure.length,
            destination = segment
        )
        drawPath(path, Color.LightGray, style = wholePathStroke.get())
        val fillBrush = if (isAmbient) ambientFillBrush else interactiveFillBrush
        drawPath(segment, fillBrush, style = progressPathStroke.get())
    }
}

private fun Path.setToSineWaveLike(size: Size) {
    if (isEmpty.not()) reset()
    val center = size.center
    val amplitude = size.height / 10f
    val start = Offset(center.x, amplitude / 2f)
    moveTo(start)
    val count = 30
    for (i in 1..count) {
        val ratio = i.toFloat() / count
        val anglePeriod = 360f / count
        quadraticBezierTo(
            p1 = start.copy(y = 0f).rotateAround(center, anglePeriod * (i - .75f)),
            p2 = start.rotateAround(pivot = center, degrees = anglePeriod * (i - .5f))
        )
        quadraticBezierTo(
            p1 = start.copy(y = amplitude).rotateAround(center, anglePeriod * (i - .25f)),
            p2 = start.rotateAround(pivot = center, degrees = anglePeriod * (i))
        )
    }
    close()
}

private fun Path.setToDecoration(size: Size) {
    val start = Offset(size.width / 2f, 0f)
    moveTo(start)
    val amplitude = size.height / 10f
    val count = 30
    for (i in 0..count) {
        val ratio = i.toFloat() / count
        val topForward = start.rotateAround(pivot = size.center, degrees = 360f * ratio)
        val bottomForward = start.copy(y = amplitude).rotateAround(size.center, 360f * ratio)
        quadraticBezierTo(
            p1 = topForward,
            p2 = bottomForward
        )
        val p3 = bottomForward.rotateAround(pivot = size.center, degrees = 360f / count / 2f)
        val p4 = topForward.rotateAround(pivot = size.center, degrees = 360f / count / 2f)
        quadraticBezierTo(
            p3, p4
        )
    }
    close()
}

private fun Path.setToAccidentalArt1(size: Size) {
    val center = size.center
    val amplitude = size.height / 10f
    val start = Offset(center.x, amplitude / 2f)
    moveTo(start)
    val count = 60
    for (i in 0..count) {
        val ratio = i.toFloat() / count
        val anglePeriod = 360f / count
        val topForward = start.rotateAround(pivot = center, degrees = 360f * ratio)
        val bottomForward = start.copy(y = amplitude).rotateAround(size.center, 360f * ratio)
        quadraticBezierTo(
            p1 = topForward,
            p2 = start.rotateAround(center, anglePeriod)
        )
        val p3 = bottomForward.rotateAround(pivot = size.center, degrees = 360f / count / 2f)
        val p4 = topForward.rotateAround(pivot = size.center, degrees = 360f / count / 2f)
        quadraticBezierTo(
            p3, p4
        )
    }
    close()
}
