package org.splitties.compose.oclock.sample.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.layer.CompositingStrategy
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize
import org.splitties.compose.oclock.OClockCanvas
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun PathPattern(
    slices: Int = 1,
    slicesCompositingStrategy: CompositingStrategy = CompositingStrategy.Offscreen,
    getRatio: () -> Float = { 1f },
    getSliceDrawStyle: SizeDependentState.Scope.() -> DrawStyle = { Fill },
    updateSlice: SizeDependentState.Scope.(Path) -> Unit,
    drawWireframeSlicePath: (DrawScope.(path: Path) -> Unit)? = null,
    drawPath: DrawScope.(path: Path) -> Unit = { drawPath(it, Color.White) },
    onDrawWireframe: ContentDrawScope.(reusedSliceLayer: GraphicsLayer) -> Unit = { drawContent() },
    onDrawWithSliceContent: ContentDrawScope.(
        reusedSliceLayer: GraphicsLayer,
        index: Int
    ) -> Unit = { _, _ -> drawContent() }
) {
    val pathMeasure = remember { PathMeasure() }
    val slicePath = remember { Path() }.rememberAsStateWithSize(updateSlice) {
        it.reset()
        updateSlice(it)
        pathMeasure.setPath(it, forceClosed = false)
    }
    val lastSegmentPath = remember { Path() }.rememberAsStateWithSize(getRatio) {
        val slicesInverse = 60f / slices
        val ratio = getRatio() * 60 % slicesInverse / slicesInverse
        val end = pathMeasure.length * ratio
        it.reset()
        pathMeasure.getSegment(startDistance = 0f, stopDistance = end, destination = it)
    }
    val sliceWireframeLayer = rememberGraphicsLayerAsState(drawWireframeSlicePath) {
        val path = slicePath.get()
        it.compositingStrategy = CompositingStrategy.Offscreen
        it.record(size = size.toIntSize()) {
            drawWireframeSlicePath?.let {
                repeat(slices) { i ->
                    rotate(360f * i / slices.toFloat()) {
                        it(path)
                    }
                }
            }
        }
    }
    val sliceLayer = rememberGraphicsLayerAsState(getSliceDrawStyle) {
        val path = slicePath.get()
        it.compositingStrategy = slicesCompositingStrategy
        if (slicesCompositingStrategy == CompositingStrategy.Offscreen) {

            val bounds = path.getBounds()
            val layerSize = bounds.size
            val width = when (val style = getSliceDrawStyle()) {
                Fill -> 0f
                is Stroke -> style.width
            }
            val halfW = width / 2f
            val offset = bounds.topLeft
            val margin = halfW + 1f
            it.record(size = (layerSize + margin * 2f).toIntSize()) {
                translate(-offset + margin) {
                    drawPath(path)
                }
            }
            offset.let { (x, y) ->
                it.translationX = x - margin
                it.translationY = y - margin
            }
        } else {
            it.record(size = size.toIntSize()) {
                drawPath(path)
            }
        }
    }
    val lastSliceLayer = rememberGraphicsLayerAsState {
        it.compositingStrategy = CompositingStrategy.Offscreen
        val path = lastSegmentPath.get()
        val bounds = path.getBounds()
        val layerSize = bounds.size
        val width = when (val style = getSliceDrawStyle()) {
            Fill -> 0f
            is Stroke -> style.width
        }
        val halfW = width / 2f
        val offset = bounds.topLeft
        val margin = halfW + 1f
        it.record(size = (layerSize + margin * 2f).toIntSize()) {
            translate(-offset + margin) {
                drawPath(path)
            }
        }
        offset.let { (x, y) ->
            it.translationX = x - margin
            it.translationY = y - margin
        }
    }
    val layerScopeFactory = remember { LayerDrawScopeFactory() }
    OClockCanvas {
        val layerDrawScope = layerScopeFactory.getLayerDrawScope(this)
        if (drawWireframeSlicePath != null) layerDrawScope.withLayer(sliceWireframeLayer.get()) {
            onDrawWireframe(it)
        }
        val ratio = getRatio()
        val last = floor(ratio * slices).roundToInt()
        for (i in 0..<slices) {
            val angle = 360f * i / slices.toFloat()
            val isLast = i == last
            rotate(angle) {
                layerDrawScope.withLayer(
                    layer = if (isLast && ratio != 1f) lastSliceLayer.get() else sliceLayer.get()
                ) {
                    onDrawWithSliceContent(it, i)
                }
            }
            if (isLast) break
        }
    }
}

private class LayerDrawScopeFactory {
    private var lastScope: LayerDrawScope? = null

    fun getLayerDrawScope(backingScope: DrawScope): LayerDrawScope {
        lastScope.let { if (it?.backingScope == backingScope) return it }
        return LayerDrawScope(backingScope).also { lastScope = it }
    }
}

private class LayerDrawScope(
    val backingScope: DrawScope
) : DrawScope by backingScope, ContentDrawScope {

    inline fun withLayer(layer: GraphicsLayer, block: ContentDrawScope.(GraphicsLayer) -> Unit) {
        this.layer = layer
        try {
            block(layer)
        } finally {
            this.layer = null
        }
    }

    private var layer: GraphicsLayer? = null
    override fun drawContent() {
        drawLayer(layer ?: return)
    }
}

private fun Rect.toIntSize(): IntSize = IntSize(
    width = ceil(width).toInt(),
    height = ceil(height).toInt()
)
