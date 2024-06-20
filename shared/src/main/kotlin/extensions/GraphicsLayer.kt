@file:Suppress("NOTHING_TO_INLINE")

package org.splitties.compose.oclock.sample.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.toIntSize

context(SizeDependentState.Scope)
inline fun GraphicsLayer.centerHorizontally() {
    translationX = scope().size.width / 2f - size.width / 2f
}

context(SizeDependentState.Scope)
inline fun GraphicsLayer.center() {
    translationX = scope().size.width / 2f - size.width / 2f
    translationY = scope().size.height / 2f - size.height / 2f
}

@Composable
fun rememberFullSizeRecordedGraphicsLayer(
    configureLayer: (GraphicsLayer) -> Unit = {},
    block: DrawScope.() -> Unit
): SizeDependentState<GraphicsLayer> {
    return rememberGraphicsLayerAsState { layer ->
        configureLayer(layer)
        layer.record(size = size.toIntSize()) {
            block()
        }
    }
}

@Composable
fun rememberGraphicsLayerAsState(
    key1: Any? = Unit,
    key2: Any? = Unit,
    block: context(SimplifiedGraphicsLayerContext) SizeDependentState.Scope.(layer: GraphicsLayer) -> Unit
): SizeDependentState<GraphicsLayer> {
    val layoutDir = LocalLayoutDirection.current
    val simplifiedGraphicsLayerContext = remember(layoutDir) {
        SimplifiedGraphicsLayerContext(layoutDir)
    }
    return rememberGraphicsLayer().rememberAsStateWithSize(key1, key2, simplifiedGraphicsLayerContext) {
        with(simplifiedGraphicsLayerContext) {
            block(this@rememberAsStateWithSize, it)
        }
    }
}

inline fun DrawScope.drawLayer(layer: SizeDependentState<GraphicsLayer>) {
    drawLayer(layer.get())
}

class SimplifiedGraphicsLayerContext internal constructor(private val layoutDir: LayoutDirection) {

    /**
     * Any previously content drawn into a [GraphicsLayer] is dropped at some point
     * when it is no longer visible (e.g. when switching to other apps),
     * and we need to re-record the content.
     *
     * To avoid this problem, we re-record every time we become visible again.
     */
    context(SizeDependentState.Scope)
    fun GraphicsLayer.record(
        size: IntSize,
        block: DrawScope.() -> Unit
    ) {
        record(
            density = asDensity(),
            layoutDirection = layoutDir,
            size = size
        ) {
            block()
        }
    }

    private fun SizeDependentState.Scope.asDensity(): Density = this
}

@PublishedApi
internal inline fun SizeDependentState.Scope.scope(): SizeDependentState.Scope = this
