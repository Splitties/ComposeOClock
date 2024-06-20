package org.splitties.compose.oclock.sample.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.layer.CompositingStrategy
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.WearPreviewSizes
import org.splitties.compose.oclock.sample.extensions.PathPattern
import org.splitties.compose.oclock.sample.extensions.SizeDependentState
import org.splitties.compose.oclock.sample.extensions.cubicTo
import org.splitties.compose.oclock.sample.extensions.moveTo
import org.splitties.compose.oclock.sample.extensions.offsetBy
import org.splitties.compose.oclock.sample.extensions.quadraticTo
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize
import org.splitties.compose.oclock.sample.extensions.rotateAround
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.sqrt

@WatchFacePreview
@Composable
fun LoopingSecondsPreview(
    @PreviewParameter(WearPreviewSizes::class) size: Dp
) = WatchFacePreview(size) {
    LoopingSeconds()
}

@Composable
fun LoopingSecondsNoCache() {
    val time = LocalTime.current
    val stroke = rememberStateWithSize { Stroke(2.dp.toPx()) }
    PathPatternNoCache(
        slices = 12,
        getRatio = { time.secondsWithMillis / 60f },
        updateSlice = { path ->
            path.setTo5minutesLoop(
                size = size,
                loopHeight = size.height / 2f * .7f,
                loopsPadding = 4.dp.toPx()
            )
        },
        drawWireframe = { path, i ->
            drawPath(path, Color.DarkGray, style = stroke.get())
        },
        drawPath = { path, i ->
            val color = colorFor5MinutesGroup(i)
            drawPath(path, color, style = stroke.get())
        }
    )
}

@Composable
fun LoopingSeconds() {
    val time = LocalTime.current
    val stroke = rememberStateWithSize<DrawStyle> { Stroke(2.dp.toPx()) }
    PathPattern(
        slices = 12,
        slicesCompositingStrategy = CompositingStrategy.Offscreen,
        getRatio = { time.secondsWithMillis / 60f },
        getSliceDrawStyle = { stroke.get() },
        updateSlice = { path ->
            path.setTo5minutesLoop(
                size = size,
                loopHeight = size.height / 2f * .7f,
                loopsPadding = 4.dp.toPx()
            )
        },
        drawWireframeSlicePath = { path ->
            drawPath(path, Color.DarkGray, style = stroke.get())
        },
        drawPath = { path ->
            val color = colorFor5MinutesGroup(0)
            drawPath(path, color, style = stroke.get())
        }
    )
}

@Composable
fun PathPatternNoCache(
    slices: Int = 12,
    getRatio: () -> Float,
    updateSlice: SizeDependentState.Scope.(Path) -> Unit,
    drawWireframe: (DrawScope.(path: Path, sliceIndex: Int) -> Unit)? = null,
    drawPath: DrawScope.(path: Path, sliceIndex: Int) -> Unit
) {
    val pathMeasure = remember { PathMeasure() }
    val slicePath = remember { Path() }.let { path ->
        rememberStateWithSize {
            path.also {
                it.reset()
                updateSlice(it)
                pathMeasure.setPath(it, forceClosed = false)
            }
        }
    }
    val lastSegmentPath = remember { Path() }.let { path ->
        rememberStateWithSize {
            path.also {
                // (44 % 5) / 5 = 0.8
                // [0-60] % [60/slices] / 60/slices
                // (44 / 60) % (1/12) / (1/12)
                val slicesInverse = 1f / slices
                val ratio = getRatio() % slicesInverse / slicesInverse
                val end = pathMeasure.length * ratio
                it.reset()
                pathMeasure.getSegment(
                    startDistance = 0f,
                    stopDistance = end,
                    destination = it
                )
            }
        }
    }
    OClockCanvas {
        drawWireframe?.let {
            repeat(slices) { i ->
                rotate(360f * i / slices.toFloat()) {
                    it(slicePath.get(), i)
                }
            }
        }
        val last = floor(getRatio() * slices).roundToInt()
        for (i in 0..<slices) {
            val angle = 360f * i / slices.toFloat()
            if (i == last) {
                rotate(angle) {
                    drawPath(lastSegmentPath.get(), i)
                }
                break
            }
            rotate(angle) {
                drawPath(slicePath.get(), i)
            }
        }
    }
}

private fun colorFor5MinutesGroup(i: Int): Color = when (i) {
    0, 1 -> Color.Green
    2, 3 -> Color.Yellow
    4, 5 -> Color.Red
    6, 7 -> Color.Cyan
    8, 9 -> Color.Magenta
    10, 11 -> Color.White
    else -> Color.LightGray
}

private fun Path.setTo5minutesLoop(size: Size, loopHeight: Float, loopsPadding: Float) {
    val angle = 360f / 12
    val halfAngle = angle / 2f

    val start = size.center.copy(y = 20f)
    val end = start.rotateAround(size.center, angle)

    val centerEdgesHeight = loopHeight * .5f
    val centerLeft = start.offsetBy(y = centerEdgesHeight)
    val centerLeftWithPadding = centerLeft.offsetBy(x = loopsPadding)

    val leftAngle = halfAngle * 1.7f
    val cpLeftTop = centerLeftWithPadding.offsetBy(y = -centerEdgesHeight).rotateAround(
        centerLeftWithPadding,
        leftAngle
    )
    val cpLeftBottom = centerLeftWithPadding.offsetBy(y = loopHeight - centerEdgesHeight).rotateAround(
        centerLeftWithPadding,
        leftAngle
    )
    val rightAngle = halfAngle - (leftAngle - halfAngle)
    val centerRightWithPadding = centerLeft.offsetBy(x = -loopsPadding).rotateAround(size.center, angle)
    val cpRightTop = centerRightWithPadding.offsetBy(y = -centerEdgesHeight).rotateAround(
        centerRightWithPadding,
        rightAngle
    )
    val cpRightBottom = centerRightWithPadding.offsetBy(y = loopHeight - centerEdgesHeight).rotateAround(
        centerRightWithPadding,
        rightAngle
    )

    moveTo(start)
    val d = sqrt(start.squaredDistanceTo(end))
    val f = .2f
    val startCp = start.offsetBy(x = d * f)
    val endCp = start.offsetBy(x = -d * f).rotateAround(size.center, angle)

    cubicTo(cp1 = startCp, cp2 = cpRightTop, point = centerRightWithPadding)
    cubicTo(cp1 = cpRightBottom, cp2 = cpLeftBottom, centerLeftWithPadding)
    cubicTo(cp1 = cpLeftTop, cp2 = endCp, end)
}

private fun Path.setTo5minutesLoopOld(size: Size, loopHeight: Float, loopsPadding: Float) {
    val start = size.center.copy(y = 20f)
    val degrees = 360f / 12
    val end = start.rotateAround(size.center, degrees)
    val middleStartEdge = start.offsetBy(y = loopHeight * .5f)
    val middleStart = middleStartEdge.offsetBy(x = loopsPadding)
    val loopTip = start.offsetBy(y = loopHeight).rotateAround(size.center, degrees / 2)
    val tipFraction = .73f
    val startTipAnchor = start.offsetBy(x = loopsPadding, y = loopHeight * tipFraction)
    val endTipAnchor = start.offsetBy(x = -loopsPadding, y = loopHeight * tipFraction).rotateAround(size.center, degrees)
    val middleEnd = middleStartEdge.offsetBy(x = -loopsPadding).rotateAround(size.center, degrees)
    moveTo(start)
    val d = sqrt(start.squaredDistanceTo(end))
    val startCp = start.offsetBy(x = d / 3)
    val endCp = start.offsetBy(x = -d / 3).rotateAround(size.center, degrees)
    cubicTo(cp1 = startCp, cp2 = end, point = middleEnd)
//    cubicTo(cp1 = loopTip, cp2 = loopTip, middleStart)
    cubicTo(cp1 = endTipAnchor, cp2 = startTipAnchor, middleStart)
    cubicTo(cp1 = start, cp2 = endCp, end)
//    moveTo(startCp)
//    lineTo(endCp)
}

private fun Float.squared() = this * this

private fun Offset.squaredDistanceTo(other: Offset): Float {
    return (other.x - x).squared() + (other.y - y).squared()
}

private fun Path.setTo5minutesLoopQuad(size: Size, loopHeight: Float) {
    val start = size.center.copy(y = 0f)
    val degrees = 360f / 12
    val end = start.rotateAround(size.center, degrees)
    val middleStart = start.offsetBy(y = loopHeight / 2f)
    val loopTip = start.offsetBy(y = loopHeight).rotateAround(size.center, degrees / 2)
//    val endTipAnchor =
    val middleEnd = middleStart.rotateAround(size.center, degrees)
    moveTo(start)
    quadraticTo(p1 = end, p2 = middleEnd)
    quadraticTo(loopTip, loopTip) //TODO:
    quadraticTo(loopTip, middleStart)
    quadraticTo(start, end)
}
