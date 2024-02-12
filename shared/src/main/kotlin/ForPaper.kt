package org.splitties.compose.oclock.sample

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.watchface.complications.data.ComplicationData
import kotlinx.coroutines.flow.*
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.OClockRootCanvas
import org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements.circlesPattern
import org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements.ovalPattern
import org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements.wavyCirclesPattern


private val gray = Color(0xff666666)
private val lightGray = Color(0xffCCCCCC)
private val blue = Color(0xFF0091EA)

@MyPreview
@Composable
private fun MultiCirclesPreview() = RootCanvas {
    OClockCanvas {
        val side = size.minDimension
        if (false) circlesPattern(
            color = gray,
            count = 6,
            diameters = listOf(.1, .15, .2, .25).map { it.toFloat() * side / 2 }
        )
        rotate(30f) {
            circlesPattern(
                color = Color(0xFF5F04AF),
                count = 12,
                diameters = listOf(.1,  .25).map { it.toFloat() * side * 2 }
            )
        }
        /*circlesPattern(
            color = blue.copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.1f
        )
        circlesPattern(
            color = _root_ide_package_.org.splitties.sample.composeoclock.lightGray.copy(alpha = .4f),
            count = 20,
            diameters = diameter,
            edgeMargin = diameter * 2.2f
        )*/
    }
}

@MyPreview
@Composable
private fun WavyCircles1Preview() = RootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        wavyCirclesPattern(color = gray, count = 180, diameters = diameter)
        wavyCirclesPattern(color = blue.copy(alpha = .8f),count = 90, diameters = diameter, edgeMargin = diameter * 1.1f)
        wavyCirclesPattern(color = lightGray.copy(alpha = .4f),count = 20, diameters = diameter, edgeMargin = diameter * 2.2f)
    }
}

@MyPreview
@Composable
private fun WavyCircles2Preview() = RootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        val factor = 6f
        wavyCirclesPattern(
            color = blue.copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.2f,
            travel = diameter / 2
        )
        wavyCirclesPattern(
            color = gray,
            count = 180,
            diameters = diameter,
            travel = diameter / factor,
            periodAngle = 15f
        )
        circlesPattern(
            color = lightGray.copy(alpha = .4f),
            count = 8,
            diameters = diameter,
            edgeMargin = diameter * 2.2f,
//            travel = diameter / factor
        )
    }
}

@MyPreview
@Composable
private fun WavyCircles3Preview() = RootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        val factor = 6f
        wavyCirclesPattern(
            color = Color(0xFF5F04AF).copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.2f,
            travel = diameter / 2
        )
        wavyCirclesPattern(
            color = gray,
            count = 180,
            diameters = diameter,
            travel = diameter / (factor / 3),
            periodAngle = 60f
        )
        circlesPattern(
            color = lightGray.copy(alpha = .4f),
            count = 8,
            diameters = diameter,
            edgeMargin = diameter * 2.2f,
//            travel = diameter / factor
        )
    }
}

@MyPreview
@Composable
private fun WavyCircles4Preview() = RootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        val factor = 6f
        if (false) wavyCirclesPattern(
            color = blue.copy(alpha = .8f),
            count = 90,
            diameters = diameter,
            edgeMargin = diameter * 1.2f,
            travel = diameter / 2
        )
        wavyCirclesPattern(
            color = Color(0xC60066FF),
            count = 7 * 30,
            diameters = diameter,
            travel = diameter / (factor / 3),
            periodAngle = 360f / 7
        )
        if (false) wavyCirclesPattern(
            color = Color(0x28CA4FF7),
            count = 5 * 30,
            travel = diameter / (factor / 3),
            periodAngle = 360f / 7,
            diameters = diameter * .4f,
            edgeMargin = diameter * 1.2f,
//            travel = diameter / factor
        )
    }
}

@MyPreview
@Composable
private fun TightCirclesPreview() = RootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        circlesPattern(color = gray, count = 180, diameters = diameter)
        circlesPattern(color = blue.copy(alpha = .8f),count = 90, diameters = diameter, edgeMargin = diameter * 1.1f)
        circlesPattern(color = lightGray.copy(alpha = .4f),count = 20, diameters = diameter, edgeMargin = diameter * 2.2f)
    }
}

@MyPreview
@Composable
private fun TightCircles2Preview() = RootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        circlesPattern(color = lightGray, count = 180, diameters = diameter, edgeMargin = 5f)
        circlesPattern(color = Color(0xFF7986CB).copy(alpha = .8f),count = 90, diameters = diameter, edgeMargin = diameter * 1.1f)
        circlesPattern(color = gray.copy(alpha = .4f),count = 20, diameters = diameter, edgeMargin = diameter * 2.1f)
    }
}

@MyPreview
@Composable
private fun TightCircles3Preview() = RootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        circlesPattern(color = lightGray, count = 90, diameters = diameter, edgeMargin = 5f)
        circlesPattern(color = Color(0xFFC7A323).copy(alpha = .8f),count = 60, diameters = diameter, edgeMargin = diameter * 1.1f)
        circlesPattern(color = gray.copy(alpha = .4f),count = 12, diameters = diameter, edgeMargin = diameter * 2.1f)
    }
}

@MyPreview
@Composable
private fun TightCirclesTimePreview() = RootCanvas {
    OClockCanvas {
        val minutes = 35
        val diameter = size.minDimension / 6
        circlesPattern(
            color = gray,
            count = 180,
            startAngle = 6f * minutes,
            diameters = diameter
        )
        circlesPattern(
            color = blue,
            count = 180,
            endAngle = 6f * minutes,
            diameters = diameter
        )
        circlesPattern(
            color = gray.copy(alpha = .8f),
            count = 12,
            diameters = diameter,
            edgeMargin = diameter * 1.1f
        )
        circlesPattern(
            color = lightGray.copy(alpha = .4f),
            count = 20,
            diameters = diameter,
            edgeMargin = diameter * 2.2f
        )
    }
}

@MyPreview
@Composable
private fun TightCirclesPreviewAlpha() = RootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        circlesPattern(color = Color(0x66333333), count = 180, diameters = diameter)
        circlesPattern(color = blue.copy(alpha = .8f),count = 90, diameters = diameter, edgeMargin = diameter * 1.1f)
        circlesPattern(color = lightGray.copy(alpha = .4f),count = 20, diameters = diameter, edgeMargin = diameter * 2.2f)
    }
}

@MyPreview
@Composable
private fun SpacedCircles() = RootCanvas {
    OClockCanvas {
        val diameter = size.minDimension / 6
        circlesPattern(Color(0xFF64DD17), count = 31, diameters = diameter)
        circlesPattern(color = Color(0xFF008800),count = 30, diameters = diameter, edgeMargin = diameter * 1.1f)
        circlesPattern(color = Color(0xFFC6FF00),count = 12, diameters = diameter, edgeMargin = diameter * 2.2f)
    }
}

@MyPreview
@Composable
private fun Rosace() = RootCanvas {
    OClockCanvas {
        val side = size.minDimension
//        circlesPattern(Color(0xFF64DD17), count = 31, diameters = diameter)
//        circlesPattern(color = Color(0xFF008800),count = 30, diameters = diameter, edgeMargin = diameter * 1.1f)
        circlesPattern(
            color = Color(0xFFFFAA16),
            count = 12,
            diameters = side / 2,
            edgeMargin = side / 6
        )
    }
}

@MyPreview
@Composable
private fun Rosace2() = RootCanvas {
    OClockCanvas {
        val side = size.minDimension
//        circlesPattern(Color(0xFF64DD17), count = 31, diameters = diameter)
//        circlesPattern(color = Color(0xFF008800),count = 30, diameters = diameter, edgeMargin = diameter * 1.1f)
        circlesPattern(
            color = Color(0xFF64DD17),
            count = 24,
            diameters = side * .8f,
            edgeMargin = side / 3
        )
    }
}

@MyPreview
@Composable
private fun Rosace3() = RootCanvas {
    OClockCanvas {
        val side = size.minDimension
//        circlesPattern(Color(0xFF64DD17), count = 31, diameters = diameter)
//        circlesPattern(color = Color(0xFF008800),count = 30, diameters = diameter, edgeMargin = diameter * 1.1f)
        circlesPattern(
            color = Color(0xFF64DD17),
            count = 24,
            diameters = side * .65f,
            edgeMargin = side / 3
        )
    }
}

@MyPreview
@Composable
private fun TightOvalsPreview() = RootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = gray,
            count = 180,
            size = size
        )
        ovalPattern(
            color = lightGray.copy(alpha = .4f),
            count = 20,
            size = size,
            edgeMargin = size.height * 2.2f
        )
    }
}

@MyPreview
@Composable
private fun TightOvalsPreviewAlpha() = RootCanvas {
    OClockCanvas {
        val side = size.minDimension
        val size = Size(
            width = side / 2,
            height = side / 6
        )
        if (false )ovalPattern(
            color = Color(0x66666666),
            count = 180,
            size = size,
            edgeMargin = 10f,
        )
        ovalPattern(
            color = Color(0xFFCDDC39),
            count = 60,
            size = Size(
                width = side / 2,
                height = side / 6
            ) * 1.9f,
            edgeMargin = size.height * 2.2f
        )
    }
}

@MyPreview
@Composable
private fun TightOvalsPreviewAlpha2() = RootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0x66333333),
            count = 180,
            size = size
        )
        ovalPattern(
            color = lightGray.copy(alpha = .4f),
            count = 20,
            size = size,
            edgeMargin = size.height * 2.2f
        )
    }
}

@MyPreview
@Composable
private fun TightOvalsPreviewAlt1() = RootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0x66333333),
            count = 90,
            size = size,
        )
        ovalPattern(
            color = lightGray.copy(alpha = .4f),
            count = 20,
            size = size * 2f,
            edgeMargin = size.height * 2.2f
        )
    }
}

@MyPreview
@Composable
private fun TightOvalsPreviewAlt2() = RootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0x66333333),
            count = 90,
            size = size,
        )
        ovalPattern(
            color = lightGray.copy(alpha = .4f),
            count = 20,
            size = size * 2f,
            edgeMargin = size.height * 2.2f
        )
    }
}

@MyPreview
@Composable
private fun TightOvalsPreviewAlt3() = RootCanvas {
    OClockCanvas {
        val size = Size(
            width = size.minDimension / 2,
            height = size.minDimension / 6
        )
        ovalPattern(
            color = Color(0x66333333),
            count = 90,
            size = size,
        )
        ovalPattern(
            color = lightGray.copy(alpha = .4f),
            count = 20,
            size = size * 2f,
            edgeMargin = size.height * 2.2f
        )
    }
}

@Preview(
    device = "id:wearos_large_round",
    backgroundColor = 0xff000000,
    showBackground = true,
    group = "Devices - Small Round",
    showSystemUi = true
)
private annotation class MyPreview


@Composable
private fun RootCanvas(
    backgroundColor: Color = Color.Transparent,
    content: @Composable (complicationData: Map<Int, StateFlow<ComplicationData>>) -> Unit
) = OClockRootCanvas(
    modifier = Modifier.fillMaxSize(),
    backgroundColor = backgroundColor,
    content = content
)

