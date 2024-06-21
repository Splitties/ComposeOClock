package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.layer.CompositingStrategy
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.util.lerp
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.elements.FatHourDigits
import org.splitties.compose.oclock.sample.extensions.angleTo
import org.splitties.compose.oclock.sample.extensions.rememberGraphicsLayerAsState
import org.splitties.compose.oclock.sample.extensions.rotateAround
import org.splitties.compose.oclock.sample.utils.FiveMinutesLayoutOrder
import org.splitties.compose.oclock.sample.utils.FiveMinutesSlicePattern

@Composable
fun LightLinesWatchFace() {
    OClockCanvas {
        drawRect(Color.Black)
    }
    val blue = Color.hsl(180f, 1f, .9f)
    val yellow = Color(0xFFF5D370)
    LightLinesMinutes(blue, cutAngle = 53f)
    FatHourDigits(interactiveColor = blue, ambientShadowRepeat = 2)
}

@Composable
fun LightLinesMinutes(
    color: Color = Color.White,
    layoutOrder: FiveMinutesLayoutOrder = FiveMinutesLayoutOrder.symmetricalSpread,
    lineStartTopPadding: Dp = 8.dp,
    lineStartBottomPadding: Dp = 74.dp,
    cutAngle: Float = -1f,
    mirrored: Boolean = true
) {
    val brush = remember { lightBrush(color) }
    val horizontalLine = rememberGraphicsLayerAsState {
        it.compositingStrategy = CompositingStrategy.Offscreen
        it.record(Size(size.width * 1.3f, 8.dp.toPx()).toIntSize()) {
            drawRect(brush)
        }
    }
    FiveMinutesSlicePattern(
        layoutOrder = layoutOrder,
        mirrored = mirrored,
        createSliceData = { slicePath, bounds ->
            val topLimit = lineStartTopPadding.toPx()
            val bottomLimit = bounds.height - lineStartBottomPadding.toPx()
            val band = bottomLimit - topLimit
            val middlePoint = Offset(x = 0f, y = topLimit + band / 2f)
            val sliceTip = Offset(x = 0f, y = bounds.height)
            val topLeftCorner = Offset.Zero
            val topRightCorner = topLeftCorner.rotateAround(sliceTip, degrees = 30f)
            val vector = topRightCorner - middlePoint
            SliceData(
                topLimit = topLimit,
                bottomLimit = bottomLimit,
                lineAngle = vector.angleTo(Offset(x = 1f, y = 0f))
            )
        },
        drawSliceContent = { i, sliceData ->
            val max = 5
            val startingPoint = lerp(
                start = sliceData.topLimit,
                stop = sliceData.bottomLimit,
                fraction = i.toFloat() / (max - 1)
            )
            val lineLayer = horizontalLine.get()
            withTransform({
                translate(top = startingPoint - lineLayer.size.height / 2f)
                rotate(sliceData.lineAngle, pivot = Offset.Zero)
                translate(left = -lineLayer.size.height.toFloat())
            }) {
                drawLayer(lineLayer)
            }
            if (cutAngle != -1f) rotate(
                degrees = sliceData.lineAngle + cutAngle, pivot = Offset.Zero
            ) {
                drawRect(
                    Color.Red,
                    size = Size(width = size.height, 40.dp.toPx()),
                    blendMode = BlendMode.Clear
                )
            }
        }
    )
}

private fun lightBrush(color: Color = Color.White): Brush {
    val transparent = color.copy(alpha = 0f)
    val mid = color.copy(alpha = .5f)
    return Brush.verticalGradient(
        0f to transparent,
        .45f to mid,
        .45f to color,
        .5f to color,
        .55f to color,
        .55f to mid,
        1f to transparent
    )
}

private class SliceData(
    val topLimit: Float,
    val bottomLimit: Float,
    val lineAngle: Float
)
