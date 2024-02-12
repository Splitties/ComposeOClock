package org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import org.splitties.compose.oclock.sample.extensions.rotate

fun DrawScope.linesPattern(
    color: Color = Color.White.copy(alpha = .4f),
    count: Int = 90
) = circularRepeat(count) { degrees ->
    drawLine(
        color = color,
        strokeWidth = 1.dp.toPx(),
        start = center.copy(y = 0f).rotate(degrees = degrees),
        end = center.copy(y = 50.dp.toPx()).rotate(degrees = degrees),
    )
}
