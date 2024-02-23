package org.splitties.compose.oclock.sample.cleanthisbeforerelease.experiments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.AndroidFont
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEachIndexed
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.WearPreviewSizesProvider
import org.splitties.compose.oclock.sample.extensions.centerAsTopLeft
import org.splitties.compose.oclock.sample.extensions.drawTextOnPath
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize
import org.splitties.compose.oclock.sample.extensions.setFrom
import org.splitties.compose.oclock.sample.extensions.text.rememberTextOnPathMeasurer
import org.splitties.compose.oclock.sample.googleFontProvider

@Composable
fun TextOnPathExperiment(finalBrush: Brush) {
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
    val textStyle = remember(fontFamily, brush) {
        TextStyle(
            brush = null,
            fontFamily = fontFamily,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600,
            textAlign = TextAlign.Start,
            lineHeight = 20.sp,
            textMotion = TextMotion.Animated,
            textGeometricTransform = TextGeometricTransform(
                scaleX = 1f,
                skewX = Float.MIN_VALUE
            )
        )
    }
    val textMeasurer = rememberTextOnPathMeasurer(cacheSize = 0)
    val isAmbient by LocalIsAmbient.current
    val cachedPath = remember { Path() }.let { path ->
        rememberStateWithSize {
            path.arcTo(
                rect = Rect(Offset.Zero, size).deflate(textStyle.fontSize.toPx()),
                startAngleDegrees = -180f,
                sweepAngleDegrees = 359f,
                forceMoveTo = true
            )
            path
        }
    }
    val txt = "Hello Romain! Bonjour Romain!".repeat(2)
    val txtList = remember {
        txt.map { c ->
            val str = c.toString()
            textMeasurer.measure(
                text = str,
                style = textStyle.copy(Color.Red)
            )
        }
    }
    val text = remember {
        textMeasurer.measure(
            text = txt,
            style = textStyle.copy(Color.White.copy(alpha = .5f))
        )
    }
    val colors = remember { listOf(Color.Gray, Color.White) }
    val paint = rememberStateWithSize {
        Paint().also {
            it.pathEffect = PathEffect.cornerPathEffect(50f)
            Brush.verticalGradient(colors,
                startY = center.y - size.height / 6f,
                endY = center.y + size.height / 6f,
            ).applyTo(size, it, 1f)
            it.alpha = 1f
            it.style = PaintingStyle.Fill
        }
    }
    val outlinePaint = rememberStateWithSize {
        Paint().also {
            it.setFrom(paint.get())
            Brush.verticalGradient(
                colors.asReversed(),
                startY = center.y - size.height / 3f,
                endY = center.y + size.height / 3f,
            ).applyTo(size, it, 1f)
            it.strokeWidth = 2.dp.toPx()
            it.style = PaintingStyle.Stroke
        }
    }
    OClockCanvas {
        val y = 0f
        val path = cachedPath.get()
//        drawPath(path, Color.Green)
        val s = size / 3f
        if (false) drawRect(
            brush = brush,
            topLeft = center.centerAsTopLeft(s),
            size = s,
            style = Stroke(
                width = 1.dp.toPx(),
                pathEffect = PathEffect.cornerPathEffect(50f)
            )
        )
        val r = Rect(center.centerAsTopLeft(s), s)
        drawContext.canvas.drawRect(r, paint.get())
        drawContext.canvas.drawRect(r, outlinePaint.get())
        txtList.fastForEachIndexed { index, textOnPathLayoutResult ->
            var offset: Float = 0f
            var i = 0
            while (i < index) {
                val current = txtList[i]
                offset += current.internalResult.getBoundingBox(current.layoutInput.text.lastIndex).right
//                offset += txtList[i].internalResult.size.width - 1
                i++
            }
            drawTextOnPath(
                textOnPathLayoutResult,
                path = path,
                offset = Offset(x = offset, y = y),
//                blendMode = BlendMode.Darken
            )
        }
        drawTextOnPath(
            text,
            path = path,
            alpha = .5f,
            offset = Offset(x = 0f, y = y),
//            blendMode = BlendMode.SrcIn
        )
    }
}

@WatchFacePreview
@Composable
private fun TextOnPathExperimentPreview(
    @PreviewParameter(WearPreviewSizesProvider::class) size: Dp
) = WatchFacePreview(size) {
    TextOnPathExperiment(finalBrush = SolidColor(Color.Magenta))
}
