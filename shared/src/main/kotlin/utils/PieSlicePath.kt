package org.splitties.compose.oclock.sample.utils

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Path
import org.splitties.compose.oclock.sample.extensions.lineTo
import org.splitties.compose.oclock.sample.extensions.moveTo

fun Path.setToPieSlice(size: Size, degrees: Float, centered: Boolean = true) {
    reset()
    moveTo(size.center)
    lineTo(size.center.copy(y = 0f))
    arcTo(size.toRect(), startAngleDegrees = -90f, sweepAngleDegrees = degrees, forceMoveTo = false)
    lineTo(size.center)
    close()
    if (centered.not()) translate(Offset(x = -size.center.x, y = 0f))
}
