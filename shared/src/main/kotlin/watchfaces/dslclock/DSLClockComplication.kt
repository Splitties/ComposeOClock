package org.splitties.compose.oclock.sample.watchfaces.dslclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.BatteryManager
import androidx.compose.runtime.Composable
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
import org.splitties.compose.oclock.sample.extensions.rotate
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun DSLClockComplication() {

    val style = remember {
        TextStyle(
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 15.sp,
        )
    }

    Background()
    TimeAndOpenParentheses(style)
    Date(style)
    Battery(style)
    HeartRate(style)
    Steps(style)
    Weather(style)
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
                "complications {",
                style
            )
        }
    }

    OClockCanvas {
        rotate(degrees = 0f) {
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = 60f,
                    y = (center.y * 0.4f) - (textLayoutResult.size.height / 2f)
                )
            )
        }
    }
}


@Composable
private fun Date(style: TextStyle) {

    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    val date = LocalDate.now().format(formatter)

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "date = $date",
                style
            )
        }
    }

    OClockCanvas {
        rotate(degrees = 0f) {
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = 120f,
                    y = (center.y * 0.6f) - (textLayoutResult.size.height / 2f)
                )
            )
        }
    }

}


@Composable
private fun Battery(style: TextStyle) {

    val context: Context = LocalContext.current
    var batteryLevel by remember { mutableIntStateOf(0) }

    val iFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
    val batteryStatus = context.registerReceiver(null, iFilter)

    batteryLevel = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1

    context.registerReceiver(
        object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context, intent: Intent) {
                try {
                    batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                } catch (_: IllegalStateException) {
                }
            }
        },
        IntentFilter(Intent.ACTION_BATTERY_CHANGED))

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "battery = $batteryLevel / 100",
                style
            )
        }
    }

    OClockCanvas {
        rotate(degrees = 0f) {
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = 120f,
                    y = (center.y * 0.8f) - (textLayoutResult.size.height / 2f)
                )
            )
        }
    }

}


@Composable
private fun HeartRate(style: TextStyle) {

    val context = LocalContext.current
    val isAmbient by LocalIsAmbient.current
    var hr by remember { mutableIntStateOf(0) }

    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val hrCounter = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

    val heartRateListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (isAmbient.not()) {
                try {
                    hr = event?.values?.get(0)?.toInt() ?: 0
                } catch (_: IllegalStateException) {
                }

            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }
    }

    hrCounter.let { sensor ->
        when (isAmbient) {
            true -> sensorManager.unregisterListener(heartRateListener)
            false -> {
                sensorManager.registerListener(
                    heartRateListener,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        }

    }


    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "heart rate = $hr bps",
                style
            )
        }
    }

    OClockCanvas {
        rotate(degrees = 0f) {
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = 120f,
                    y = center.y - (textLayoutResult.size.height / 2f)
                )
            )
        }
    }

}


@Composable
private fun Steps(style: TextStyle) {

    var steps by remember { mutableIntStateOf(-1) }

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "steps = $steps",
                style
            )
        }
    }

    OClockCanvas {
        rotate(degrees = 0f) {
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = 120f,
                    y = size.height - (center.y * 0.8f) - (textLayoutResult.size.height / 2f)
                )
            )
        }
    }

}


@Composable
private fun Weather(style: TextStyle) {

    val weather = "no idea"

    @OptIn(InternalComposeOClockApi::class)
    val measurer = LocalTextMeasurerWithoutCache.current

    val textLayoutResult by remember {
        derivedStateOf {
            measurer.measure(
                "weather = $weather",
                style
            )
        }
    }

    OClockCanvas {
        rotate(degrees = 0f) {
            drawText(
                brush = style.brush ?: SolidColor(style.color),
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = 120f,
                    y = size.height - (center.y * 0.6f) - (textLayoutResult.size.height / 2f)
                )
            )
        }
    }

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
                topLeft = Offset(
                    x = 60f,
                    y = size.height - (center.y * 0.4f) - (textLayoutResult.size.height / 2f)
                )
            )
        }
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
