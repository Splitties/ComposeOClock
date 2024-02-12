package org.splitties.compose.oclock.sample.cleanthisbeforerelease.experiments

import android.graphics.RenderNode
import android.os.Build.VERSION.SDK_INT
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.wear.compose.ui.tooling.preview.WearPreviewSmallRound
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.OClockRootCanvas
import org.splitties.compose.oclock.TapEvent
import org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements.circlesPattern
import org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements.ovalPattern

@Composable
fun CirclesExperiment() {
    var index by remember { mutableIntStateOf(1) }
    OClockCanvas(
        onTap = { event ->
            val squaredMaxDistance = 48f.squared().dp.toPx()
            val squaredDistanceTo = event.position.squaredDistanceTo(center)
            if (squaredDistanceTo <= squaredMaxDistance) {
                if (event is TapEvent.Up) index++
                true
            } else false
        }
    ) {
        if (SDK_INT >= 29) {
            val node = RenderNode("")
            val recordingCanvas = node.beginRecording(size.width.toInt(), size.height.toInt())
            recordingCanvas
            drawIntoCanvas { it.nativeCanvas.drawRenderNode(node) }

        }
        when (index) {
            0 -> {}
            1 -> {
                val minutes = 35
                val diameter = size.minDimension / 6
                circlesPattern(
                    color = Color(0xff666666),
                    count = 180,
                    startAngle = 6f * minutes,
                    diameters = diameter
                )
                circlesPattern(
                    color = Color(0xFF0091EA),
                    count = 180,
                    endAngle = 6f * minutes,
                    diameters = diameter
                )
                circlesPattern(
                    color = Color(0xff666666).copy(alpha = .8f),
                    count = 12,
                    diameters = diameter,
                    edgeMargin = diameter * 1.1f
                )
                circlesPattern(
                    color = Color.White.copy(alpha = .4f),
                    count = 20,
                    diameters = diameter,
                    edgeMargin = diameter * 2.2f
                )
            }
            2 -> {
                val diameter = size.minDimension / 6
                circlesPattern(color = Color(0xff666666), count = 180, diameters = diameter)
                circlesPattern(color = Color(0xFF0091EA).copy(alpha = .8f),count = 90, diameters = diameter, edgeMargin = diameter * 1.1f)
                circlesPattern(color = Color.White.copy(alpha = .4f),count = 20, diameters = diameter, edgeMargin = diameter * 2.2f)
            }
            3 -> {
                val diameter = size.minDimension / 6
                circlesPattern(color = Color(0x66ffffff), count = 180, diameters = diameter)
                circlesPattern(color = Color(0xFF0091EA).copy(alpha = .8f),count = 90, diameters = diameter, edgeMargin = diameter * 1.1f)
                circlesPattern(color = Color.White.copy(alpha = .4f),count = 20, diameters = diameter, edgeMargin = diameter * 2.2f)
            }
            4 -> {
                drawCircle(Color.Gray)
            }
            5 -> {
                val diameter = size.minDimension / 6
                circlesPattern(Color(0xFF64DD17), count = 31, diameters = diameter)
                circlesPattern(color = Color(0xFF008800),count = 30, diameters = diameter, edgeMargin = diameter * 1.1f)
                circlesPattern(color = Color(0xFFC6FF00),count = 12, diameters = diameter, edgeMargin = diameter * 2.2f)
            }
            6 -> {
                val size = Size(
                    width = size.minDimension / 2,
                    height = size.minDimension / 6
                )
                ovalPattern(
                    color = Color(0xff666666),
                    count = 180,
                    size = size
                )
                ovalPattern(
                    color = Color.White.copy(alpha = .4f),
                    count = 20,
                    size = size,
                    edgeMargin = size.height * 2.2f
                )
            }
            7 -> {
                val size = Size(
                    width = size.minDimension / 2,
                    height = size.minDimension / 6
                )
                ovalPattern(
                    color = Color(0x66666666),
                    count = 180,
                    size = size
                )
                ovalPattern(
                    color = Color.White.copy(alpha = .4f),
                    count = 20,
                    size = size,
                    edgeMargin = size.height * 2.2f
                )
            }
            8 -> {
                val size = Size(
                    width = size.minDimension / 2,
                    height = size.minDimension / 6
                )
                ovalPattern(
                    color = Color(0x66ffffff),
                    count = 180,
                    size = size
                )
                ovalPattern(
                    color = Color.White.copy(alpha = .4f),
                    count = 20,
                    size = size,
                    edgeMargin = size.height * 2.2f
                )
            }
            9 -> {
                val size = Size(
                    width = size.minDimension / 2,
                    height = size.minDimension / 6
                )
                ovalPattern(
                    color = Color(0x66ffffff),
                    count = 90,
                    size = size,
                )
                ovalPattern(
                    color = Color.White.copy(alpha = .4f),
                    count = 20,
                    size = size * 2f,
                    edgeMargin = size.height * 2.2f
                )
            }
            10 -> {
                val size = Size(
                    width = size.minDimension / 2,
                    height = size.minDimension / 6
                )
                ovalPattern(
                    color = Color(0x66666666),
                    count = 90,
                    size = size,
                )
                ovalPattern(
                    color = Color.White.copy(alpha = .4f),
                    count = 20,
                    size = size * 2f,
                    edgeMargin = size.height * 2.2f
                )
            }
            11 -> {
                val size = Size(
                    width = size.minDimension / 2,
                    height = size.minDimension / 6
                )
                ovalPattern(
                    color = Color(0x66ffffff),
                    count = 90,
                    size = size,
                )
                ovalPattern(
                    color = Color.White.copy(alpha = .4f),
                    count = 20,
                    size = size * 2f,
                    edgeMargin = size.height * 2.2f
                )
            }
            else -> index = 0
        }
        drawIntoCanvas {
            println("hardwareAccelerated: ${it.nativeCanvas.isHardwareAccelerated}")
        }
    }
}

private fun Float.squared() = this * this

private fun Offset.squaredDistanceTo(other: Offset): Float {
    return (other.x - x).squared() + (other.y - y).squared()
}

@WearPreviewSmallRound
@Composable
private fun CirclesExperimentPreview() = OClockRootCanvas {
    CirclesExperiment()
}
