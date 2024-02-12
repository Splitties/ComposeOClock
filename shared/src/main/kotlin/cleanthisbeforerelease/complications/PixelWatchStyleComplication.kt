package org.splitties.compose.oclock.sample.cleanthisbeforerelease.complications

import android.app.PendingIntent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.EmptyComplicationData
import androidx.wear.watchface.complications.data.GoalProgressComplicationData
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.data.MonochromaticImageComplicationData
import androidx.wear.watchface.complications.data.NoDataComplicationData
import androidx.wear.watchface.complications.data.NoPermissionComplicationData
import androidx.wear.watchface.complications.data.NotConfiguredComplicationData
import androidx.wear.watchface.complications.data.PhotoImageComplicationData
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.SmallImageComplicationData
import androidx.wear.watchface.complications.data.WeightedElementsComplicationData
import kotlinx.coroutines.flow.*
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.complications.rememberDrawableAsState
import org.splitties.compose.oclock.complications.rememberMeasuredAsState
import org.splitties.compose.oclock.sample.extensions.topCenterAsTopLeft

@Composable
fun PixelWatchStyleComplication(
    complicationDataFlow: StateFlow<ComplicationData>,
    textStyle: TextStyle,
    sizeRatio: Float
) {
    val time = LocalTime.current
    val resources = LocalContext.current.resources
    val complicationData by complicationDataFlow.collectAsState()
    OClockCanvas(onTap = {
        try {
            complicationData.tapAction?.send()
        } catch (e: PendingIntent.CanceledException) {
            // In case the PendingIntent is no longer able to execute the request.
            // We don't need to do anything here.
        }
        false
    }) {}
    when (val data = complicationData) {
        is EmptyComplicationData -> {}
        is NoDataComplicationData -> {
            data.placeholder
            data.contentDescription
        }
        is NoPermissionComplicationData -> {
            data.rememberDrawableAsState()
            data.text
            data.title
        }
        is NotConfiguredComplicationData -> {}
        is LongTextComplicationData -> {
            val text by data.text.rememberMeasuredAsState { string ->
                measure(text = string, style = textStyle)
            }
            val title by data.title.rememberMeasuredAsState("") { string ->
                measure(text = string, style = textStyle)
            }
            val image by data.rememberDrawableAsState()
            data.contentDescription
        }
        is MonochromaticImageComplicationData -> {
            data.rememberDrawableAsState()
            data.contentDescription
        }
        is PhotoImageComplicationData -> {
            data.rememberDrawableAsState()
            data.contentDescription
        }
        is RangedValueComplicationData -> {
            data.valueType
            RangedValueComplicationData.TYPE_RATING
            RangedValueComplicationData.TYPE_PERCENTAGE
            RangedValueComplicationData.TYPE_UNDEFINED
            data.min
            data.max
            data.value
            data.colorRamp
            data.title
            data.text
            data.rememberDrawableAsState()
            data.contentDescription
            val text by data.text.rememberMeasuredAsState("") { string ->
                measure(text = string, style = textStyle)
            }
            val title by data.title.rememberMeasuredAsState("") { string ->
                measure(text = string, style = textStyle)
            }
            OClockCanvas {
                drawArc(
                    color = textStyle.color,
                    startAngle = -90f,
                    sweepAngle = 360 * data.value / data.max,
                    useCenter = false,
                    style = Stroke(width = 10f, cap = StrokeCap.Round),
                    blendMode = BlendMode.Screen
                )
                drawText(
                    textLayoutResult = title,
                    topLeft = center.copy(
                        y = 20f
                    ).topCenterAsTopLeft(title.size)
                )
                drawText(
                    textLayoutResult = text,
                    topLeft = center.copy(
                        y = 20f - title.size.height
                    ).topCenterAsTopLeft(text.size)
                )
            }
        }
        is ShortTextComplicationData -> {
            data.rememberDrawableAsState()
            data.text
            data.title
            data.contentDescription
        }
        is SmallImageComplicationData -> {
            data.rememberDrawableAsState()
            data.contentDescription
        }
        else -> if (Build.VERSION.SDK_INT >= 33) when (data) {
            is GoalProgressComplicationData -> {
                data.targetValue
                data.value
                data.value
                data.colorRamp
                data.text
                data.title
                data.rememberDrawableAsState()
                data.contentDescription
            }
            is WeightedElementsComplicationData -> {
                data.elements.forEach {
                    it.color
                    it.weight
                }
                data.elementBackgroundColor
                data.rememberDrawableAsState()
                data.title
                data.text
                data.contentDescription
            }
            else -> {}
        }
    }
}
