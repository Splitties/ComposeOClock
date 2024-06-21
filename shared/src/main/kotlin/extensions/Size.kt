package org.splitties.compose.oclock.sample.extensions

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import kotlin.math.floor
import kotlin.math.min


operator fun Size.minus(pixels: Float): Size {
    return Size(width - pixels, height - pixels)
}

operator fun Size.plus(pixels: Float): Size {
    return Size(width + pixels, height + pixels)
}

fun Size.Companion.square(pixels: Float): Size {
    return Size(pixels, pixels)
}

/**
 * Returns the largest Size that would fit into [other], while keeping the aspect ratio.
 */
fun Size.fitIn(other: Size): Size {
    val maxFactor = other.maxDimension / maxDimension
    val minFactor = other.minDimension / minDimension
    val factor = min(maxFactor, minFactor)
    return this * factor
}

fun Size.toFlooredIntSize(): IntSize {
    return IntSize(floor(width).toInt(), floor(height).toInt())
}
