package org.splitties.compose.oclock.sample.watchfaces.dslclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTextMeasurerWithoutCache
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.internal.InternalComposeOClockApi
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.WearPreviewSizesProvider
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun DSLClockComplication() {

    val textStyle = remember {
        TextStyle(
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp,
        )
    }

    Background()
    TimeAndOpenParentheses(textStyle)
    Date(textStyle)
    Battery(textStyle)
    Steps(textStyle)
    Weather(textStyle)
    CloseParentheses(textStyle)

}


@Composable
private fun Background() {
    val isAmbient by LocalIsAmbient.current
    OClockCanvas {
        if (isAmbient.not()) drawCircle(kotlinDarkBg)
    }
}

@Composable
private fun TimeAndOpenParentheses(textStyle: TextStyle) {

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "comp {",
                textStyle
            )
        }
    }

    OClockCanvas {
        drawText(
            brush = textStyle.brush ?: SolidColor(textStyle.color),
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = center.x * 0.23f,
                y = (center.y * 0.45f) - (textLayoutResult.size.height / 2f)
            )
        )
    }
}


@Composable
private fun Date(textStyle: TextStyle) {

    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val date = LocalDate.now().format(formatter)

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "date = $date",
                textStyle
            )
        }
    }

    OClockCanvas {
        drawText(
            brush = textStyle.brush ?: SolidColor(textStyle.color),
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = center.x * 0.4f,
                y = (center.y * 0.7f) - (textLayoutResult.size.height / 2f)
            )
        )
    }

}


@Composable
private fun Battery(textStyle: TextStyle) {

    val context: Context = LocalContext.current
    var batteryLevel by remember { mutableIntStateOf(0) }

    DisposableEffect(true) {
        val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, iFilter)

        batteryLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1

        val batteryListener = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                try {
                    batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                } catch (_: IllegalStateException) {
                }
            }
        }

        context.registerReceiver(
            batteryListener,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        onDispose {
            context.unregisterReceiver(batteryListener)
        }
    }

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "battery = $batteryLevel / 100",
                textStyle
            )
        }
    }

    OClockCanvas {
        drawText(
            brush = textStyle.brush ?: SolidColor(textStyle.color),
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = center.x * 0.4f,
                y = (center.y * 0.9f) - (textLayoutResult.size.height / 2f)
            )
        )
    }

}


@Composable
private fun Steps(textStyle: TextStyle) {

    var steps by remember { mutableIntStateOf(-1) }

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "steps = $steps",
                textStyle
            )
        }
    }

    OClockCanvas {
        drawText(
            brush = textStyle.brush ?: SolidColor(textStyle.color),
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = center.x * 0.4f,
                y = size.height - (center.y * 0.9f) - (textLayoutResult.size.height / 2f)
            )
        )
    }

}


@Composable
private fun Weather(textStyle: TextStyle) {

    val weather = ""

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "weather = $weather",
                textStyle
            )
        }
    }

    OClockCanvas {
        drawText(
            brush = textStyle.brush ?: SolidColor(textStyle.color),
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = center.x * 0.4f,
                y = size.height - (center.y * 0.7f) - (textLayoutResult.size.height / 2f)
            )
        )
    }

}


@Composable
private fun CloseParentheses(textStyle: TextStyle) {
    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "}",
                textStyle
            )
        }
    }

    OClockCanvas {
        drawText(
            brush = textStyle.brush ?: SolidColor(textStyle.color),
            textLayoutResult = textLayoutResult,
            topLeft = Offset(
                x = center.x * 0.3f,
                y = size.height - (center.y * 0.45f) - (textLayoutResult.size.height / 2f)
            )
        )
    }
}

private val kotlinDarkBg = Color(0xFF101010)

@WatchFacePreview
@Composable
private fun KotlinComplicationDSLClockPreview(
    @PreviewParameter(WearPreviewSizesProvider::class) size: Dp,
) = WatchFacePreview(size) {
    DSLClockComplication()
}
