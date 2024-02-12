package org.splitties.compose.oclock.sample.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.IntSize
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

context (DrawScope)
fun Offset.rotate(degrees: Float): Offset {
    return rotateAround(pivot = center, degrees = degrees)
}

fun Offset.rotateAround(
    pivot: Offset,
    degrees: Float,
): Offset {
    val angle = Math.toRadians(degrees.toDouble()).toFloat()
    val cx = pivot.x
    val cy = pivot.y
    val offsetX = x - cx // Offset backwards to match the center of the Unit Circle.
    val offsetY = y - cy // Offset backwards to match the center of the Unit Circle.

    val offsetRotatedX = offsetX * cos(angle) - offsetY * sin(angle)
    val offsetRotatedY = offsetX * sin(angle) + offsetY * cos(angle)
    return Offset(
        x = offsetRotatedX + cx, // Offset back, forward.
        y = offsetRotatedY + cy // Offset back, forward.
    )
}

fun Offset.centerAsTopLeft(size: Size): Offset {
    return copy(x = x - size.width / 2f, y = y - size.height / 2f)
}

fun Offset.centerAsTopLeft(side: Float): Offset {
    return copy(x = x - side / 2f, y = y - side / 2f)
}

fun Offset.centerAsTopLeft(size: IntSize): Offset {
    return copy(x = x - size.width / 2f, y = y - size.height / 2f)
}

fun Offset.topCenterAsTopLeft(size: Size): Offset {
    return copy(x = x - size.width / 2f)
}

fun Offset.topCenterAsTopLeft(size: IntSize): Offset {
    return copy(x = x - size.width / 2f)
}

fun Offset.topCenterAsTopLeft(width: Float): Offset {
    return copy(x = x - width / 2f)
}

operator fun Offset.plus(amount: Float): Offset = copy(x = x + amount, y = y + amount)
operator fun Offset.plus(size: Size): Offset = copy(x = x + size.width, y = y + size.height)

operator fun Offset.minus(size: Size): Offset = copy(x = x - size.width, y = y - size.height)
operator fun Offset.minus(amount: Float): Offset = copy(x = x - amount, y = y - amount)

fun Offset.offsetBy(x: Float = 0f, y: Float = 0f): Offset {
    return copy(x = this.x + x, y = this.y + y)
}

fun Offset.angleTo(other: Offset): Float {
    return Math.toDegrees(atan2(
        y = y  * other.x - x * other.y,
        x = x * other.x + y * other.y
    ).toDouble()).toFloat()
}
