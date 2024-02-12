package org.splitties.compose.oclock.sample.extensions

import android.graphics.Paint
import android.graphics.drawable.Drawable
import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnitType
import androidx.core.graphics.drawable.updateBounds
import extensions.AndroidTextPaint
import extensions.setAlpha
import org.splitties.compose.oclock.sample.extensions.text.TextOnPathLayoutResult

fun DrawScope.drawOval(
    color: Color,
    size: Size = this.size,
    center: Offset = this.center,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode
) = drawOval(
    color = color,
    topLeft = center.centerAsTopLeft(size),
    alpha = alpha,
    size = size,
    style = style,
    colorFilter = colorFilter,
    blendMode = blendMode
)

fun DrawScope.drawArc(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    useCenter: Boolean,
    size: Size = this.size,
    center: Offset = this.center,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode
) = drawArc(
    color = color,
    topLeft = center.centerAsTopLeft(size),
    startAngle = startAngle,
    sweepAngle = sweepAngle,
    useCenter = useCenter,
    alpha = alpha,
    size = size,
    style = style,
    colorFilter = colorFilter,
    blendMode = blendMode
)

fun DrawScope.drawPainter(
    painter: Painter,
    topLeft: Offset = Offset.Zero,
    size: Size = painter.intrinsicSize,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    withTransform({
        translate(topLeft)
    }) {
        with(painter) {
            draw(size, alpha, colorFilter)
        }
    }
}

fun DrawScope.drawTextOnPath(
    textLayoutResult: TextOnPathLayoutResult,
    path: Path,
    offset: Offset = Offset.Zero,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
    blendMode: BlendMode = DrawScope.DefaultBlendMode
) {
    drawContext.canvas.nativeCanvas.drawTextOnPath(
        textLayoutResult.layoutInput.text.text,
        path.asAndroidPath(),
        offset.x,
        offset.y,
        textPaint.also {
            it.setFrom(
                textLayoutResult = textLayoutResult,
                blendMode = blendMode,
                alpha = alpha
            )
        }
    )
}

private val textPaint = AndroidTextPaint(
    flags = Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG,
    density = 1f
)

context(DrawScope)
internal fun AndroidTextPaint.setFrom(
    textLayoutResult: TextOnPathLayoutResult,
    blendMode: BlendMode,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float,
) {
    val layoutInput = textLayoutResult.layoutInput
    val style = layoutInput.style
    this.density = layoutInput.density.density
    setColor(style.color)
    setBrush(style.brush, size)
    setShadow(style.shadow)
    setDrawStyle(style.drawStyle ?: Fill)
    this.blendMode = blendMode
    when (style.fontSize.type) {
        TextUnitType.Sp -> textSize = style.fontSize.toPx()
        TextUnitType.Em -> {
            textSize *= style.fontSize.value
        }
        else -> {} // Do nothing
    }
    textAlign = style.textAlign.toPaintAlign(layoutDirection)
    setTextDecoration(style.textDecoration)

    setAlpha(alpha)

    // See internal fun AndroidTextPaint.applySpanStyle in TextPaintExtensions.android.kt
    typeface = textLayoutResult.typeface
    fontFeatureSettings = style.fontFeatureSettings
    val textGeometricTransform = style.textGeometricTransform
        textScaleX = textGeometricTransform?.scaleX ?: 1f
        textSkewX = textGeometricTransform?.skewX ?: 0f
    if (style.letterSpacing.type == TextUnitType.Sp && style.letterSpacing.value != 0.0f) {
        val emWidth = textSize * textScaleX
        val letterSpacingPx = style.letterSpacing.toPx()
        // Do nothing if emWidth is 0.0f.
        if (emWidth != 0.0f) {
            letterSpacing = letterSpacingPx / emWidth
        }
    } else if (style.letterSpacing.type == TextUnitType.Em) {
        letterSpacing = style.letterSpacing.value
    }
}

private fun TextAlign.toPaintAlign(layoutDirection: LayoutDirection): Paint.Align = when (this) {
    TextAlign.Left -> Paint.Align.LEFT
    TextAlign.Right -> Paint.Align.RIGHT
    TextAlign.Center -> Paint.Align.CENTER
    TextAlign.Start -> when (layoutDirection) {
        LayoutDirection.Ltr -> Paint.Align.LEFT
        LayoutDirection.Rtl -> Paint.Align.RIGHT
    }
    TextAlign.End -> when (layoutDirection) {
        LayoutDirection.Ltr -> Paint.Align.RIGHT
        LayoutDirection.Rtl -> Paint.Align.LEFT
    }
    else -> TextAlign.Start.toPaintAlign(layoutDirection)
}

//TODO: Remove when https://issuetracker.google.com/issues/318384666 is fixed.
inline fun DrawScope.rotate(
    degrees: Float,
    pivot: Offset = center,
    block: DrawScope.() -> Unit
) = withTransform({ rotate(degrees, pivot) }, block)

//TODO: Remove when https://issuetracker.google.com/issues/318384666 is fixed.
inline fun DrawScope.withTransform(
    transformBlock: DrawTransform.() -> Unit,
    drawBlock: DrawScope.() -> Unit
) = with(drawContext) {
    // Transformation can include inset calls which change the drawing area
    // so cache the previous size before the transformation is done
    // and reset it afterwards
    val previousSize = size
    canvas.save()
    try {
        transformBlock(transform)
        drawBlock()
    } finally {
        canvas.restore()
        size = previousSize
    }
}

fun DrawScope.drawDrawable(
    drawable: Drawable,
    topLeft: Offset = Offset.Zero,
    size: Size = this.size
) {
    val left = topLeft.x
    val top = topLeft.y
    val right = left + size.width
    val bottom = top + size.height
    drawable.updateBounds(
        left = left.toInt(),
        top = top.toInt(),
        right = right.toInt(),
        bottom = bottom.toInt(),
    )
    drawable.draw(drawContext.canvas.nativeCanvas)
}
