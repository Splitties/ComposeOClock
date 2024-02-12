package org.splitties.compose.oclock.sample.elements

import androidx.annotation.FloatRange
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import org.splitties.compose.oclock.sample.extensions.cubicTo
import org.splitties.compose.oclock.sample.extensions.lineTo
import org.splitties.compose.oclock.sample.extensions.moveTo
import org.splitties.compose.oclock.sample.extensions.offsetBy
import org.splitties.compose.oclock.sample.extensions.plus
import org.splitties.compose.oclock.sample.extensions.quadraticBezierTo
import org.splitties.compose.oclock.sample.extensions.rotateAround
import kotlin.math.sqrt

fun Path.setToStar4(
    side: Float,
    topLeft: Offset = Offset.Zero,
) {
    setToStarX(count = 6, side = side, topLeft = topLeft)
}

fun Path.setToStarX(
    count: Int,
    side: Float,
    topLeft: Offset = Offset.Zero,
) {
    if (isEmpty.not()) reset()
    val half = side / 2f
    val topMiddle = topLeft.offsetBy(x = half)
    val center = topLeft + half
    moveTo(topMiddle)
    for (i in 1..count) {
        val target = topMiddle.rotateAround(pivot = center, degrees = 360f * i / count)
        quadraticBezierTo(center, target)
    }
    close()
}

fun Path.setToHeart(
    topLeft: Offset = Offset.Zero,
    size: Size,
    @FloatRange(.0, 1.65) tipSharpnessRatio: Float = 1.1f
) {
    if (isEmpty.not()) reset()
    val halfH = size.height / 2f
    val halfW = size.width / 2f
    val topControl = halfH * .68f
    val tipControl = halfH * tipSharpnessRatio
    val topMiddle = topLeft.offsetBy(
        x = halfW,
        y = halfW * .5f
    )
    moveTo(topMiddle)
    cubicTo(
        topMiddle.offsetBy(y = -topControl),
        topMiddle.offsetBy(x = -halfW, y = -topControl),
        topMiddle.offsetBy(x = -halfW) // Left
    )
    arcTo(Rect(topMiddle, topMiddle.offsetBy(x = -halfW)), startAngleDegrees = 0f, -180f, forceMoveTo = false)
    cubicTo(
        topMiddle.offsetBy(x = -halfW, y = topControl),
        topMiddle.offsetBy(y = tipControl),
        topMiddle.copy(y = topLeft.y + size.height) // Bottom
    )
    cubicTo(
        topMiddle.offsetBy(y = tipControl),
        topMiddle.offsetBy(x = halfW, y = topControl),
        topMiddle.offsetBy(x = halfW) // Right
    )
    cubicTo(
        topMiddle.offsetBy(x = halfW, y = -topControl),
        topMiddle.offsetBy(y = -topControl),
        topMiddle // Top
    )
    close()
}

fun Path.setToSketchyHeart(
    size: Size,
    topLeft: Offset = Offset.Zero,
) {
    if (isEmpty.not()) reset()
    val halfH = size.height / 2f
    val halfW = size.width / 2f
    val circlesRadius = size.width / 4f
    val topPoint = topLeft.offsetBy(x = halfW, y = circlesRadius)
    val bottomMiddle = topLeft.offsetBy(x = halfW, y = size.height)
    moveTo(bottomMiddle)
    val bottomControl = topLeft.offsetBy(x = halfW, y = size.height * .8f)
    val leftEdge = topLeft.offsetBy(y = size.height / 4)
    val rightEdge = topLeft.offsetBy(x = size.width,y = size.height / 4)
    cubicTo(
        cp1 = bottomMiddle,
        cp2 = bottomControl,
        point = bottomMiddle
    )
    cubicTo(
        cp1 = leftEdge.offsetBy(y = circlesRadius),
        cp2 = leftEdge.offsetBy(y = - circlesRadius),
        point = leftEdge
    )
    val topMiddle = topLeft.offsetBy(x = halfW)
    val topLeftEdge = topMiddle.offsetBy(x = -circlesRadius)
    val topRightEdge = topMiddle.offsetBy(x = circlesRadius)
    cubicTo(
        cp1 = topLeft,
        cp2 = topLeft.offsetBy(x = halfW),
        point = topLeftEdge
    )
    cubicTo(
        cp1 = topMiddle,
        cp2 = topPoint,
        point = topPoint
    )
    cubicTo(
        cp1 = topPoint,
        cp2 = topMiddle,
        point = topRightEdge
    )
    cubicTo(
        cp1 = rightEdge.offsetBy(y = -circlesRadius),
        cp2 = rightEdge.offsetBy(y = circlesRadius),
        point = rightEdge
    )
    cubicTo(
        cp1 = bottomControl,
        cp2 = bottomMiddle,
        point = bottomMiddle
    )
    close()
}

fun Path.setToKotlinLogo(
    side: Float,
    topLeft: Offset = Offset.Zero,
) {
    if (isEmpty.not()) reset()
    moveTo(topLeft)
    lineTo(topLeft.offsetBy(x = side))
    lineTo(topLeft.offsetBy(x = side / 2f, y = side / 2f))
    lineTo(topLeft.offsetBy(x = side, y = side))
    lineTo(topLeft.offsetBy(y = side))
    close()
}

fun Path.setToKotlinLogo(
    side: Float,
    topLeft: Offset = Offset.Zero,
    stroke: Stroke? = null,
) {
    val strokeWidth = stroke?.width ?: 0f
    val halfStroke = strokeWidth / 2f
    val endOffset = if (stroke != null) sqrt(halfStroke * halfStroke * 2) else 0f
    if (isEmpty.not()) reset()
    val logoCenterX = side / 2f - endOffset
    moveTo(topLeft + halfStroke)
    lineTo(topLeft.offsetBy(x = side - endOffset - halfStroke, y = halfStroke))
    lineTo(topLeft.offsetBy(x = logoCenterX, y = side / 2f))
    lineTo(topLeft.offsetBy(x = side - endOffset - halfStroke, y = side - halfStroke))
    lineTo(topLeft.offsetBy(x = halfStroke, y = side - halfStroke))
    close()
}
