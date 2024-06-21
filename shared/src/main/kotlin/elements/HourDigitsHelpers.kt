package org.splitties.compose.oclock.sample.elements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import org.splitties.compose.oclock.LocalTime

@Composable
fun rememberHourDigits(textStyle: TextStyle): TextLayoutResult {
    val time = LocalTime.current
    val textMeasurer = rememberTextMeasurer()
    val measuredText = remember(time.hours, textStyle) {
        textMeasurer.measure(text = "${time.hours}", style = textStyle)
    }
    return measuredText
}
