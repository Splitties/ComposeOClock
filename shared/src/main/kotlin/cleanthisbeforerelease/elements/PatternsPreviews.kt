package org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.ui.tooling.preview.WearPreviewSmallRound
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.OClockRootCanvas
import org.splitties.compose.oclock.sample.extensions.rotate

@WearPreviewSmallRound
@Composable
private fun MultiCirclesPreview() = OClockRootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 2
        circlesPattern(
            color = Color(0xff666666),
            count = 6,
            diameters = listOf(.1, .15, .2, .25).map { it.toFloat() * diameter }
        )
        rotate(30f) {
            circlesPattern(
                color = Color(0xff666666),
                count = 6,
                diameters = listOf(.1, .15, .2, .25).map { it.toFloat() * diameter * 2f }
            )
        }
        /*circlesPattern(
            color = Color(0xFF0091EA).copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.1f
        )
        circlesPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            diameters = diameter,
            edgeMargin = diameter * 2.2f
        )*/
    }
}

@WearPreviewSmallRound
@Composable
private fun WavyCircles1Preview() = OClockRootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        wavyCirclesPattern(color = Color(0xff666666), count = 180, diameters = diameter)
        wavyCirclesPattern(
            color = Color(0xFF0091EA).copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.1f
        )
        wavyCirclesPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            diameters = diameter,
            edgeMargin = diameter * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun WavyCircles2Preview() = OClockRootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        val factor = 6f
        wavyCirclesPattern(
            color = Color(0xFF0091EA).copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.2f,
            travel = diameter / 2
        )
        wavyCirclesPattern(
            color = Color(0xff666666),
            count = 180,
            diameters = diameter,
            travel = diameter / factor,
            periodAngle = 15f
        )
        circlesPattern(
            color = Color.White.copy(alpha = .4f),
            count = 8,
            diameters = diameter,
            edgeMargin = diameter * 2.2f,
//            travel = diameter / factor
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun WavyCircles3Preview() = OClockRootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        val factor = 6f
        wavyCirclesPattern(
            color = Color(0xFF0091EA).copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.2f,
            travel = diameter / 2
        )
        wavyCirclesPattern(
            color = Color(0xff666666),
            count = 180,
            diameters = diameter,
            travel = diameter / (factor / 3),
            periodAngle = 60f
        )
        circlesPattern(
            color = Color.White.copy(alpha = .4f),
            count = 8,
            diameters = diameter,
            edgeMargin = diameter * 2.2f,
//            travel = diameter / factor
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun TightCirclesPreview() = OClockRootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        circlesPattern(color = Color(0xff666666), count = 180, diameters = diameter)
        circlesPattern(
            color = Color(0xFF0091EA).copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.1f
        )
        circlesPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            diameters = diameter,
            edgeMargin = diameter * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun TightCirclesTimePreview() = OClockRootCanvas {
    OClockCanvas {
        val minutes = 35
        val diameter = size.minDimension / 6
        circlesPattern(
            color = Color(0xff666666),
            count = 180,
            startAngle = 6f * minutes,
            diameters = diameter
        )
        circlesPattern(
            color = Color(0xFF0091EA),
            count = 180,
            endAngle = 6f * minutes,
            diameters = diameter
        )
        circlesPattern(
            color = Color(0xff666666).copy(alpha = .8f),
            count = 12,
            diameters = diameter,
            edgeMargin = diameter * 1.1f
        )
        circlesPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            diameters = diameter,
            edgeMargin = diameter * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun TightCirclesPreviewAlpha() = OClockRootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        circlesPattern(color = Color(0x66ffffff), count = 180, diameters = diameter)
        circlesPattern(
            color = Color(0xFF0091EA).copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.1f
        )
        circlesPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            diameters = diameter,
            edgeMargin = diameter * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun SpacedCircles() = OClockRootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        circlesPattern(Color(0xFF64DD17), count = 31, diameters = diameter)
        circlesPattern(
            color = Color(0xFF008800),
            count = 30,
            diameters = diameter,
            edgeMargin = diameter * 1.1f
        )
        circlesPattern(
            color = Color(0xFFC6FF00),
            count = 12,
            diameters = diameter,
            edgeMargin = diameter * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun TightOvalsPreview() = OClockRootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0xff666666),
            count = 180,
            size = size
        )
        ovalPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            size = size,
            edgeMargin = size.height * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun TightOvalsPreviewAlpha() = OClockRootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0x66666666),
            count = 180,
            size = size
        )
        ovalPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            size = size,
            edgeMargin = size.height * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun TightOvalsPreviewAlpha2() = OClockRootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0x66ffffff),
            count = 180,
            size = size
        )
        ovalPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            size = size,
            edgeMargin = size.height * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun TightOvalsPreviewAlt1() = OClockRootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0x66ffffff),
            count = 90,
            size = size,
        )
        ovalPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            size = size * 2f,
            edgeMargin = size.height * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun TightOvalsPreviewAlt2() = OClockRootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0x66666666),
            count = 90,
            size = size,
        )
        ovalPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            size = size * 2f,
            edgeMargin = size.height * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun TightOvalsPreviewAlt3() = OClockRootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0x66ffffff),
            count = 90,
            size = size,
        )
        ovalPattern(
            color = Color.White.copy(alpha = .4f),
            count = 20,
            size = size * 2f,
            edgeMargin = size.height * 2.2f
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun JustLines() = OClockRootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        linesPattern(
            color = Color(0x66ffffff),
            count = 90,
        )
    }
}

@WearPreviewSmallRound
@Composable
private fun Lines1() = OClockRootCanvas {
    OClockCanvas {
        val strokeWidth = 1.dp.toPx()
        val offset = 20.dp.toPx()
        run {
            val start = center.run { copy(x = x - offset, y = 0f) }
            val end = center.run { copy(x = x + offset, y = y - offset) }
            circularRepeat(60) { degrees ->
                drawLine(
                    color = Color(0x66ffffff),
                    strokeWidth = strokeWidth,
                    start = start.rotate(degrees),
                    end = end.rotate(degrees)
                )
            }
        }
        run {
            val start = center.run { copy(x = x + offset, y = 0f) }
            val end = center.run { copy(x = x + offset, y = y - offset) }
            circularRepeat(60) { degrees ->
                drawLine(
                    color = Color(0x66ffffff),
                    strokeWidth = strokeWidth,
                    start = start.rotate(degrees),
                    end = end.rotate(degrees)
                )
            }
        }
    }
}

@WearPreviewSmallRound
@Composable
private fun Lines2() = OClockRootCanvas {
    OClockCanvas {
//        val color = Color(0x66ffffff)
        val color = Color(0xff666666)
        val strokeWidth = 1.dp.toPx()
        val offset = 50.dp.toPx()
//        run {
//            val start = center.run { copy(x = x - offset, y = 0f) }
//            val end = center.run { copy(x = x + offset, y = y - offset) }
//            circularRepeat(60) { degrees ->
//                drawLine(
//                    color = color,
//                    strokeWidth = strokeWidth,
//                    start = start.rotate(degrees),
//                    end = end.rotate(degrees)
//                )
//            }
//        }
//        run {
//            val start = center.run { copy(x = x + offset, y = 0f) }
//            val end = center.run { copy(x = x - offset, y = y - offset) }
//            circularRepeat(60) { degrees ->
//                drawLine(
//                    color = color,
//                    strokeWidth = strokeWidth,
//                    start = start.rotate(degrees),
//                    end = end.rotate(degrees)
//                )
//            }
//        }
        val radius = size.minDimension / 6f
        circularRepeat(60) { degrees ->
            drawLine(
                color = color,
                strokeWidth = strokeWidth,
                start = center.run { copy(x = x - offset, y = 0f) }.rotate(degrees),
                end = center.run { copy(x = x - offset, y = size.minDimension) }.rotate(degrees)
            )
        }
    }
}
