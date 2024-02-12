package org.splitties.compose.oclock.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.AndroidFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.extensions.drawTextOnPath
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize
import org.splitties.compose.oclock.sample.extensions.text.rememberTextOnPathMeasurer

@Composable
fun ComposeOClockWatermark(finalBrush: Brush) {
    val font = remember {
        Font(
            googleFont = GoogleFont("Jost"),
//            googleFont = GoogleFont("Raleway"),
//            googleFont = GoogleFont("Reem Kufi"),
//            googleFont = GoogleFont("Montez"),
            fontProvider = googleFontProvider,
        ) as AndroidFont
    }
    val fontFamily = remember(font) { FontFamily(font) }
    val context = LocalContext.current
    val brush by produceState(finalBrush, font) {
        val result = runCatching {
            font.typefaceLoader.awaitLoad(context, font)
        }
        value = if (result.isSuccess) finalBrush else Brush.linearGradient(listOf(Color.Red))
    }
    val interactiveTextStyle = remember(fontFamily, brush) {
        TextStyle.Default.copy(
            brush = brush,
            fontFamily = fontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            textMotion = TextMotion.Animated
        )
    }
    val ambientTextStyle = rememberStateWithSize(interactiveTextStyle) {
        interactiveTextStyle.copy(fontWeight = FontWeight.W300)
    }
    val textMeasurer = rememberTextOnPathMeasurer(cacheSize = 0)
    val isAmbient by LocalIsAmbient.current
    val cachedPath = remember { Path() }.let { path ->
        rememberStateWithSize {
            val minimumInset = interactiveTextStyle.fontSize.toPx()
            path.arcTo(
                rect = Rect(Offset.Zero, size).deflate(minimumInset + 4.dp.toPx()),
                startAngleDegrees = 89.5f,
                sweepAngleDegrees = 359f,
                forceMoveTo = true
            )
            path
        }
    }
    val string = "It's Compose O'Clock!"
    val interactiveText = rememberStateWithSize {
        textMeasurer.measure(
            text = string,
            style = interactiveTextStyle
        )
    }
    val ambientText = rememberStateWithSize {
        textMeasurer.measure(
            text = string,
            style = ambientTextStyle.get()
        )
    }
    OClockCanvas {
        val text = if (isAmbient) ambientText else interactiveText
        drawTextOnPath(
            textLayoutResult = text.get(),
            path = cachedPath.get()
        )
    }
}

@WatchFacePreview
@Composable
private fun ComposeOClockWatermarkPreview(
    @PreviewParameter(WearPreviewSizesProvider::class) size: Dp
) = WatchFacePreview(size) {
    ComposeOClockWatermark(SolidColor(Color.Magenta))
}
