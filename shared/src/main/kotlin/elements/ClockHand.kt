package org.splitties.compose.oclock.sample.elements

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

fun DrawScope.clockHand(
    color: Color,
    width: Float = 10.dp.toPx(),
    height: Float = size.height / 4f,
    style: DrawStyle = Fill,
    blendMode: BlendMode
) {
    val halfWidth = width / 2f
    val finalHeight = height + halfWidth
    drawRoundRect(
        color = color,
        topLeft = Offset(
            x = center.x - width / 2f,
            y = center.y - finalHeight + width / 2f
        ),
        size = Size(width, finalHeight),
        cornerRadius = CornerRadius(width),
        style = style,
        blendMode = blendMode
    )
}

fun DrawScope.clockHand(
    brush: Brush,
    width: Float = 10.dp.toPx(),
    height: Float = size.height / 4f,
    style: DrawStyle = Fill,
    blendMode: BlendMode = BlendMode.SrcOver
) {
    val padding = if (style is Stroke) style.width else 0f
    val finalWidth = width - padding
    val halfWidth = finalWidth / 2f
    val finalHeight = height + halfWidth - padding / 2f
    drawRoundRect(
        brush = brush,
        topLeft = Offset(
            x = center.x - halfWidth,
            y = center.y - finalHeight + halfWidth
        ),
        size = Size(finalWidth, finalHeight),
        cornerRadius = CornerRadius(finalWidth),
        style = style,
        blendMode = blendMode
    )
}
