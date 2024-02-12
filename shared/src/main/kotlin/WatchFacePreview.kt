package org.splitties.compose.oclock.sample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.watchface.complications.data.ComplicationData
import kotlinx.coroutines.flow.*
import org.splitties.compose.oclock.OClockRootCanvas

class WearPreviewSizesProvider : PreviewParameterProvider<Dp> {
    override val values: Sequence<Dp> = sequenceOf(
        WatchFacePreview.Size.small,
        WatchFacePreview.Size.large
    )
}

/**
 * Example usage:
 *
 * ```kotlin
 * @WatchFacePreview
 * @Composable
 * private fun MyFunClockPreview(
 *     @PreviewParameter(WearPreviewSizesProvider::class) size: Dp
 * ) = WatchFacePreview(size) {
 *     MyFunClock()
 * }
 * ```
 */
@Preview
annotation class WatchFacePreview {
    object Size {
        val small = 192.dp
        val large = 227.dp
    }
}

@Composable
fun WatchFacePreview(
    size: Dp,
    content: @Composable (complicationData: Map<Int, StateFlow<ComplicationData>>) -> Unit
) {
    val spacing = 8.dp
    var touched by remember { mutableStateOf(false) }
    val isAmbientFlow = remember { MutableStateFlow(false) }
    val isAmbient by isAmbientFlow.collectAsState()
    val backgroundColor by animateColorAsState(
        targetValue = when {
            touched.not() -> Color.Transparent
            isAmbient -> Color.DarkGray
            else -> Color.LightGray
        }
    )
    Column(
        modifier = Modifier.background(backgroundColor).clickable {
            touched = true
            isAmbientFlow.update { it.not() }
        }.padding(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        val modifier = Modifier.requiredSize(size).clip(CircleShape)
        OClockRootCanvas(
            modifier = modifier,
            isAmbientFlow = isAmbientFlow
        ) { content(it) }
        AnimatedVisibility(touched.not()) {
            OClockRootCanvas(
                modifier = modifier,
                isAmbientFlow = remember { MutableStateFlow(true) }
            ) { content(it) }
        }
    }
}
