package org.splitties.compose.oclock.sample.watchfaces.hansie

import android.icu.text.MessageFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.splitties.compose.oclock.CurrentTime
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTextMeasurerWithoutCache
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.internal.InternalComposeOClockApi
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.extensions.rotate
import java.util.Locale

@Suppress("DEPRECATION")
@Composable
fun HansieClock(locale: Locale = LocalConfiguration.current.locale) {
    val isAmbient by LocalIsAmbient.current
    HansieBackground()
    if (isAmbient.not()) {
        HansieSecondsHand(locale)
    }
    HansieMinutesHand(locale)
    HansieHoursHand(locale)
    HansieCenterDot()
}

@Composable
private fun HansieBackground() {
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        if (isAmbient.not()) drawCircle(kotlinDarkBg)
    }
}

@Composable
private fun HansieHoursHand(locale: Locale) {
    val style = remember {
        TextStyle(
            fontSize = 26.sp,
            fontWeight = FontWeight.W600,
            color = Color.White
        )
    }
    HansieHand(locale, style, HansieHandType.Hour)
}

@Composable
private fun HansieMinutesHand(locale: Locale) {
    val style = remember {
        TextStyle(
            fontSize = 26.sp,
            fontWeight = FontWeight.W400,
            color = Color.DarkGray.copy(alpha = 0.8f),
            textGeometricTransform = TextGeometricTransform(
                scaleX = 1.25f,
                skewX = Float.MIN_VALUE
            ),
        )
    }

    HansieHand(locale, style, HansieHandType.Minute)
}

@Composable
private fun HansieSecondsHand(locale: Locale) {
    val style = remember {
        TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.W200,
            color = Color.LightGray.copy(alpha = 0.6f),
            textGeometricTransform = TextGeometricTransform(
                scaleX = 1.50f,
                skewX = Float.MIN_VALUE
            ),
        )
    }

    HansieHand(locale, style, HansieHandType.Second)
}

@Composable
private fun HansieHand(
    locale: Locale,
    style: TextStyle,
    type: HansieHandType
) {
    val time = LocalTime.current

    val timeFormatter = remember(locale) {
        MessageFormat(
            "{0,spellout,full}",
            locale
        )
    }
    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember(timeFormatter) {
        derivedStateOf {
            val handValueInt = type.handValue(time).toInt()
            val timeText = timeFormatter.format(arrayOf(handValueInt))
            measurer.measure(
                timeText,
                style
            )
        }
    }

    OClockCanvas {
        val handValue = type.handValue(time)
        val degrees = (handValue * type.factor) - 90f
        rotate(degrees = degrees) {
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = center.plus(Offset(25f, -textLayoutResult.size.height / 2f))
            )
        }
    }
}

enum class HansieHandType(val factor: Float) {
    Hour(30f) {
        override fun handValue(current: CurrentTime): Float = current.hours.toFloat()
    },
    Minute(6f) {
        override fun handValue(current: CurrentTime): Float = current.minutes.toFloat()
    },
    Second(6f) {
        override fun handValue(current: CurrentTime): Float = current.seconds.toFloat()
    };

    abstract fun handValue(current: CurrentTime): Float
}

@Composable
private fun HansieCenterDot() {
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        drawCircle(kotlinDarkBg, radius = 6.dp.toPx(), blendMode = BlendMode.Xor)
        drawCircle(
            color = if (isAmbient) Color.Black else kotlinLogoColors[2],
            radius = 5.dp.toPx()
        )
        if (isAmbient) {
            drawCircle(
                Color.Gray,
                radius = 5.dp.toPx()
            )
            drawCircle(
                Color.Black,
                radius = 2.dp.toPx()
            )
        }
    }
}

private val kotlinDarkBg = Color(0xFF1B1B1B)
private val kotlinBlue = Color(0xFF7F52FF)
private val kotlinLogoColors = listOf(
    kotlinBlue,
    Color(0xFF_C811E2),
    Color(0xFF_E54857),
)

@WatchFacePreview
@Composable
private fun HansieClockPreview(
) = WatchFacePreview(WatchFacePreview.Size.large) {
    HansieClock(Locale.US)
}

@WatchFacePreview
@Composable
private fun HansieClockPreviewFr(
) = WatchFacePreview(WatchFacePreview.Size.large) {
    HansieClock(Locale.FRENCH)
}

@WatchFacePreview
@Composable
private fun HansieClockPreviewSa(
) = WatchFacePreview(WatchFacePreview.Size.large) {
    HansieClock(Locale("af", "ZA"))
}
