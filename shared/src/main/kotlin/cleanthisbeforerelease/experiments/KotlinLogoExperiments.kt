package org.splitties.compose.oclock.sample.cleanthisbeforerelease.experiments

import androidx.annotation.FloatRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements.circularRepeat
import org.splitties.compose.oclock.sample.extensions.centerAsTopLeft
import org.splitties.compose.oclock.sample.extensions.cubicTo
import org.splitties.compose.oclock.sample.extensions.lineTo
import org.splitties.compose.oclock.sample.extensions.moveTo
import org.splitties.compose.oclock.sample.extensions.offsetBy
import org.splitties.compose.oclock.sample.extensions.plus
import org.splitties.compose.oclock.sample.extensions.quadraticBezierTo
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize
import org.splitties.compose.oclock.sample.extensions.rotate
import org.splitties.compose.oclock.sample.extensions.rotateAround
import kotlin.math.sqrt

@WatchFacePreview
@Composable
private fun KotlinLogoExperimentsPreview() = WatchFacePreview(
    WatchFacePreview.Size.small
) {
    KotlinLogoExperiments()
}

@Composable
fun KotlinLogoExperiments() {
    val smallKotlinLogoPath = remember { Path() }
    val bigKotlinLogoPath = remember { Path() }
    val edgePadding = 0.dp
    val isAmbient by LocalIsAmbient.current
    val cachedBigStroke = rememberStateWithSize { Stroke(4f.dp.toPx()) }
    val cachedBigLogoGradient = rememberStateWithSize {
        val bigStroke by cachedBigStroke
        val side = size.minDimension / 3f
        val topLeft = center.centerAsTopLeft(side)
        bigKotlinLogoPath.setToKotlinLogo(
            side,
            topLeft,
            stroke = if (isAmbient) bigStroke else null
        )
        Brush.linearGradient(
            kotlinLogoColors,
            start = topLeft.run { copy(y = y + side) },
            end = topLeft.run { copy(x = x + side) }
        )
    }
    val cachedSmallLogosGradient = rememberStateWithSize {
        val side = size.minDimension / 8f
        val topLeft = Offset(x = center.x, y = side / 2f + edgePadding.toPx()).centerAsTopLeft(side)
        smallKotlinLogoPath.setToStar4(
            side = side,
//            size = Size(side, side),
//            thickness = side / 5f,
            topLeft = topLeft
        )
        Brush.linearGradient(
            kotlinLogoColors,
            start = topLeft.run { copy(y = y + side) },
            end = topLeft.run { copy(x = x + side) }
        )
    }
    val cachedStroke = rememberStateWithSize {
        Stroke(
            1.5f.dp.toPx(),
            cap = StrokeCap.Butt,
            join = StrokeJoin.Miter
        )
        Stroke(1.5f.dp.toPx(), cap = StrokeCap.Butt, join = StrokeJoin.Miter)
    }
    OClockCanvas {
//        if (isAmbient.not()) {
//            drawRect(kotlinDarkBg)
//        }
        cachedSmallLogosGradient.get()
        val stroke by cachedStroke
        val bigStroke by cachedBigStroke
        val smallLogosGradient by cachedSmallLogosGradient
        val bigLogosGradient by cachedBigLogoGradient
        circularRepeat(24) {
            rotate(it) {
                drawPath(
                    smallKotlinLogoPath,
                    color = Color.Cyan,
                    style = stroke
                )
            }
        }
        drawPath(
            bigKotlinLogoPath,
            brush = bigLogosGradient,
            style = if (isAmbient) bigStroke else Fill
        )
        drawCircle(
            brush = bigLogosGradient,
            blendMode = BlendMode.SrcIn
        )
    }
}

private val kotlinDarkBg = Color(0xFF1B1B1B)
private val kotlinBlue = Color(0xFF7F52FF)
private val kotlinLogoColors = listOf(
    kotlinBlue,
    Color(0xFF_C811E2),
    Color(0xFF_E54857),
)

private fun Path.setToLLetter(
    side: Float,
    smallSide: Float = side * .6f,
    thickness: Float,
    topLeft: Offset = Offset.Zero,
) {
    if (isEmpty.not()) reset()
    moveTo(topLeft)
    lineTo(topLeft.offsetBy(x = thickness))
    lineTo(topLeft.offsetBy(x = thickness, y = side - thickness))
    lineTo(topLeft.offsetBy(x = smallSide, y = side - thickness))
    lineTo(topLeft.offsetBy(x = smallSide, y = side))
    lineTo(topLeft.offsetBy(y = side))
    close()
}

private fun Path.setToStar4(
    side: Float,
    topLeft: Offset = Offset.Zero,
) {
    setToStarX(count = 4, side = side, topLeft = topLeft)
}

private fun Path.setToStarX(
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

private fun Path.setToHeart(
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

private fun Path.setToSketchyHeart(
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

private fun Path.setToKotlinLogo(
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

private fun Path.setToKotlinLogo(
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
