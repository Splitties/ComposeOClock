package org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.splitties.compose.oclock.sample.extensions.drawOval
import org.splitties.compose.oclock.sample.extensions.rotate
import kotlin.math.PI
import kotlin.math.sin

fun DrawScope.circlesPattern(
    color: Color = Color.White.copy(alpha = .4f),
    count: Int = 90,
    startAngle: Float = 0f,
    endAngle: Float = 360f,
    diameters: Float,
    edgeMargin: Float = 0f,
) {
    val stroke = Stroke(width = 1.dp.toPx())
    val radius = diameters / 2
    for (i in 1..count) {
        val angle = 360f * i / count
        if (angle < startAngle) continue
        if (angle > endAngle) return
        drawCircle(
            color = color,
            style = stroke,
            radius = radius,
            center = center.copy(y = edgeMargin + stroke.width / 2 + radius).rotate(angle)
        )
    }
}

fun DrawScope.wavyCirclesPattern(
    color: Color = Color.White.copy(alpha = .4f),
    count: Int = 90,
    startAngle: Float = 0f,
    endAngle: Float = 360f,
    diameters: Float,
    travel: Float = diameters / 2f,
    periodAngle: Float = 30f,
    edgeMargin: Float = 0f,
) {
    val stroke = Stroke(width = 1.dp.toPx())
    val radius = diameters / 2
    for (i in 1..count) {
        val angle = 360f * i / count
        if (angle < startAngle) continue
        if (angle > endAngle) return
        val periodicOffset = (travel * (sin(2 * PI * (angle % periodAngle) / periodAngle - PI / 2) + 1)) / 2
        val y = edgeMargin + stroke.width / 2 + radius + periodicOffset.toFloat()
        drawCircle(
            color = color,
            style = stroke,
            radius = radius,
            center = center.copy(y = y).rotate(angle)
        )
    }
}

fun DrawScope.circlesPattern(
    color: Color = Color.White.copy(alpha = .4f),
    count: Int = 90,
    diameters: List<Float>,
    edgeMargin: Float = 0f,
) {
    val stroke = Stroke(width = 1.dp.toPx())
    diameters.forEach { diameter ->
        val radius = diameter / 2
        repeat(count) { i ->
            val angle = 360f * i / count
            drawCircle(
                color = color,
                style = stroke,
                radius = radius,
                center = center.copy(y = edgeMargin + stroke.width / 2 + radius).rotate(angle)
            )
        }
    }
}

fun DrawScope.ovalPattern(
    color: Color = Color.White.copy(alpha = .4f),
    count: Int = 90,
    size: Size,
    edgeMargin: Float = 0f,
) {
    val stroke = Stroke(width = 1.dp.toPx())
    repeat(count) { i ->
        rotate(degrees = 360f * i / count) {
            drawOval(
                color = color,
                style = stroke,
                size = size,
                center = center.copy(y = edgeMargin + stroke.width / 2 + size.height / 2)
            )
        }
    }
}

fun DrawScope.straightOvalPattern(
    color: Color = Color.White.copy(alpha = .4f),
    count: Int = 90,
    size: Size,
    edgeMargin: Float = 0f,
) {
    val stroke = Stroke(width = 1.dp.toPx())
    repeat(count) { i ->
        drawOval(
            color = color,
            style = stroke,
            size = size,
            center = center.copy(
                y = edgeMargin + stroke.width / 2 + size.height / 2
            ).rotate(degrees = 360f * i / count)
        )
    }
}
