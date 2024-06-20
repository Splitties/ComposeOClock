package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.TapEvent
import org.splitties.compose.oclock.sample.WatchFacePreview
import org.splitties.compose.oclock.sample.WearPreviewSizes

@Composable
fun WatchFaceSwitcher() {
    var index by remember { mutableIntStateOf(0) }
    OClockCanvas(
        onTap = handler@{ event ->
            val touchEdgeWidth = 48.dp.toPx()
            val isOnLeftEdge = event.position.x <= touchEdgeWidth
            val isOnRightEdge = isOnLeftEdge.not() &&
                    event.position.x >= size.width - touchEdgeWidth
            if (isOnLeftEdge.not() && isOnRightEdge.not()) return@handler false
            if (event is TapEvent.Up) {
                val lastIndex = allWatchFaces.lastIndex
                index = when {
                    isOnLeftEdge -> index - 1
                    else -> index + 1
                }.let {
                    when {
                        it < 0 -> lastIndex
                        it > lastIndex -> 0
                        else -> it
                    }
                }
            }
            true
        }
    ) { }
    allWatchFaces[index]()
}

@WatchFacePreview
@Composable
private fun WatchFaceSwitcherPreview(
    @PreviewParameter(WearPreviewSizes::class) size: Dp
) = WatchFacePreview(size) {
    WatchFaceSwitcher()
}
