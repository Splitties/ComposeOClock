package org.splitties.compose.oclock.sample.utils

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.layer.CompositingStrategy
import androidx.compose.ui.graphics.layer.drawLayer
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.extensions.SizeDependentState
import org.splitties.compose.oclock.sample.extensions.rememberGraphicsLayerAsState
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize
import org.splitties.compose.oclock.sample.extensions.toFlooredIntSize

@Composable
fun <T> FiveMinutesSlicePattern(
    layoutOrder: FiveMinutesLayoutOrder = FiveMinutesLayoutOrder.symmetricalSpread,
    mirrored: Boolean = true,
    createSliceData: SizeDependentState.Scope.(slicePath: Path, bounds: Rect) -> T,
    drawSliceContent: DrawScope.(fiveMinutesIndex: Int, sliceData: T) -> Unit
) {
    val slicePathAndBounds = rememberStateWithSize {
        val path = Path().also {
            it.setToPieSlice(size, 30f, centered = false)
        }
        path to path.getBounds()
    }
    val sliceData = rememberStateWithSize {
        val (path, bounds) = slicePathAndBounds.get()
        createSliceData(path, bounds)
    }
    val minuteLayers = List(5) { i ->
        minuteSliceLayer(
            slicePath = slicePathAndBounds,
            sliceData = sliceData,
            fiveMinutesIndex = i,
            drawSliceContent = drawSliceContent
        )
    }
    val time = LocalTime.current
    OClockCanvas {
        val max = 12
        val minutes = time.minutes
        for (i in 0..<max) {
            val isLast = i == (minutes / 5)
            if (isLast && minutes % 5 == 0) break
            val normalRotation = mirrored.not() || i % 2 == 0
            val angle = 30f * if (normalRotation) i else (i + 1)
            rotate(angle) {
                scale(
                    scaleX = if (normalRotation) 1f else -1f,
                    scaleY = 1f
                ) {
                    for (j in 0..4) {
                        val index = if (normalRotation) j else 4 - j
                        if (isLast) {
                            val shouldShowIt = layoutOrder.showFor(
                                minute = minutes,
                                minuteMarkIndex = j
                            )
                            if (shouldShowIt.not()) continue
                        }
                        val layer = minuteLayers[index].get()
                        drawLayer(layer)
                    }
                }
            }
            if (isLast) break
        }
    }
}

@Composable
private fun <T> minuteSliceLayer(
    slicePath: SizeDependentState<Pair<Path, Rect>>,
    sliceData: SizeDependentState<T>,
    @IntRange(0, 4) fiveMinutesIndex: Int,
    drawSliceContent: DrawScope.(fiveMinutesIndex: Int, sliceData: T) -> Unit
) = rememberGraphicsLayerAsState { layer ->
    require(fiveMinutesIndex in 0..4)
    val (path, bounds) = slicePath.get()
    layer.compositingStrategy = CompositingStrategy.Offscreen
    layer.setPathOutline(path)
    layer.clip = true
    layer.translationX = center.x
    @Suppress("name_shadowing") val sliceData = sliceData.get()
    layer.record(size = bounds.size.toFlooredIntSize()) {
        drawSliceContent(fiveMinutesIndex, sliceData)
    }
}
