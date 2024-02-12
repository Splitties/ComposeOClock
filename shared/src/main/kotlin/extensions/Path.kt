@file:Suppress("NOTHING_TO_INLINE")

package org.splitties.compose.oclock.sample.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path

inline fun Path.moveTo(position: Offset) {
    moveTo(x = position.x, y = position.y)
}

inline fun Path.lineTo(position: Offset) {
    lineTo(x = position.x, y = position.y)
}

inline fun Path.quadraticBezierTo(
    p1: Offset,
    p2: Offset
) = quadraticBezierTo(
    x1 = p1.x,
    y1 = p1.y,
    x2 = p2.x,
    y2 = p2.y
)

inline fun Path.cubicTo(
    cp1: Offset,
    cp2: Offset,
    point: Offset
) = cubicTo(
    x1 = cp1.x,
    y1 = cp1.y,
    x2 = cp2.x,
    y2 = cp2.y,
    x3 = point.x,
    y3 = point.y
)
