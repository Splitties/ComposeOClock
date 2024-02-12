package org.splitties.compose.oclock.sample.elements.vectors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

@Composable
fun rememberWearOsLogoVectorPainter(): VectorPainter {
    return rememberVectorPainter(remember { wearOsIcon() })
}

fun wearOsIcon(): ImageVector = ImageVector.Builder(
    name = "WearOsIcon",
    defaultWidth = 469.7.dp,
    defaultHeight = 359.dp,
    viewportWidth = 469.7f,
    viewportHeight = 359f
).apply {
    path(
        fill = SolidColor(Color(0xFF00A94B)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(418.59999999999997f, 146.4f)
        arcTo(
            48.7f,
            48.7f,
            0f,
            isMoreThanHalf = false,
            isPositiveArc = true,
            369.9f,
            195.10000000000002f
        )
        arcTo(48.7f, 48.7f, 0f, isMoreThanHalf = false, isPositiveArc = true, 321.2f, 146.4f)
        arcTo(
            48.7f,
            48.7f,
            0f,
            isMoreThanHalf = false,
            isPositiveArc = true,
            418.59999999999997f,
            146.4f
        )
        close()
    }
    path(
        fill = SolidColor(Color(0xFFFF4131)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(469.7f, 46.3f)
        arcTo(45.5f, 45.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 424.2f, 91.8f)
        arcTo(45.5f, 45.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 378.7f, 46.3f)
        arcTo(45.5f, 45.5f, 0f, isMoreThanHalf = false, isPositiveArc = true, 469.7f, 46.3f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFFFFBC00)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(305.5f, 359f)
        curveToRelative(-17.4f, 0f, -34.1f, -10.1f, -41.6f, -27f)
        lineTo(144.6f, 64.1f)
        curveToRelative(-10.2f, -23f, 0.1f, -49.9f, 23.1f, -60.1f)
        curveToRelative(23f, -10.2f, 49.9f, 0.1f, 60.1f, 23.1f)
        lineToRelative(119.3f, 267.9f)
        curveToRelative(10.2f, 23f, -0.1f, 49.9f, -23.1f, 60.1f)
        curveTo(318f, 357.8f, 311.7f, 359f, 305.5f, 359f)
        close()
    }
    path(
        fill = SolidColor(Color(0xFF0085F7)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.NonZero
    ) {
        moveTo(164.7f, 358.3f)
        curveToRelative(-19f, 0f, -37.1f, -11f, -45.3f, -29.4f)
        lineTo(4.3f, 70.3f)
        curveTo(-6.8f, 45.3f, 4.4f, 16f, 29.4f, 4.9f)
        reflectiveCurveTo(83.7f, 5f, 94.8f, 30f)
        lineToRelative(115.1f, 258.6f)
        curveToRelative(11.1f, 25f, -0.1f, 54.3f, -25.1f, 65.4f)
        curveTo(178.3f, 356.9f, 171.4f, 358.3f, 164.7f, 358.3f)
        close()
    }
}.build()

