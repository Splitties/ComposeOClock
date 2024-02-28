package org.splitties.compose.oclock.sample.watchfaces.dslclock

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import org.splitties.compose.oclock.CurrentTime
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTextMeasurerWithoutCache
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.internal.InternalComposeOClockApi
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.WearPreviewSizesProvider
import org.splitties.compose.oclock.sample.extensions.rotate

@Composable
fun DSLClockTime() {

    val style = remember {
        TextStyle(
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp,
        )
    }

    Background()
    TimeAndOpenParentheses(style)
    Hours(style)
    Minutes(style)
    Seconds(style)
    CloseParentheses(style)

}


@Composable
private fun Background() {
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        if (isAmbient.not()) drawCircle(kotlinDarkBg)
    }
}

@Composable
private fun TimeAndOpenParentheses(style: TextStyle) {

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "time {",
                style
            )
        }
    }

    OClockCanvas {
        rotate(degrees = 0f) {
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = Offset(x = 60f, y = (center.y * 0.55f) - (textLayoutResult.size.height / 2f))
            )
        }
    }
}

@Composable
private fun Hours(style: TextStyle) {

    DSLTimeValue(
        style = style,
        timeType = TimeParamType.Hour,
    )
}


@Composable
private fun Minutes(style: TextStyle) {

    DSLTimeValue(
        style = style,
        timeType = TimeParamType.Minute,
    )
}

@Composable
private fun Seconds(style: TextStyle) {

    DSLTimeValue(
        style = style,
        timeType = TimeParamType.Second,
    )
}


@Composable
private fun CloseParentheses(style: TextStyle) {
    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "}",
                style
            )
        }
    }

    OClockCanvas {
        rotate(degrees = 0f) {
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = Offset(x = 60f, y = size.height - (center.y * 0.55f)  - (textLayoutResult.size.height / 2f))
            )
        }
    }
}


@Composable
private fun DSLTimeValue(
    style: TextStyle,
    timeType: TimeParamType,
) {
    val time = LocalTime.current

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            val timeInt = timeType.paramValue(time)
            measurer.measure(
                "${timeType.paramName} = $timeInt",
                style
            )
        }
    }

    OClockCanvas {
        rotate(degrees = 0f) {
            val offset = when (timeType) {
                TimeParamType.Hour -> Offset(x = 140f, y = (center.y * 0.8f) - (textLayoutResult.size.height / 2f))
                TimeParamType.Minute -> Offset(x = 140f, y = center.y - (textLayoutResult.size.height / 2f))
                TimeParamType.Second -> Offset(x = 140f, y = size.height - (center.y * 0.8f) - (textLayoutResult.size.height / 2f))
            }
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = offset
            )
        }
    }
}


enum class TimeParamType(val paramName: String) {
    Hour("hours") {
        override fun paramValue(current: CurrentTime): Int = current.hours
    },
    Minute("minutes") {
        override fun paramValue(current: CurrentTime): Int = current.minutes
    },
    Second("seconds") {
        override fun paramValue(current: CurrentTime): Int = current.seconds
    };

    abstract fun paramValue(current: CurrentTime): Int
}

private val kotlinDarkBg = Color(0xFF101010)

@WatchFacePreview
@Composable
private fun KotlinTimeDSLClockPreview(
    @PreviewParameter(WearPreviewSizesProvider::class) size: Dp,
) = WatchFacePreview(size) {
    DSLClockTime()
}
