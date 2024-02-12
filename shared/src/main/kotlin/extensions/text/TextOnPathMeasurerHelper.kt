package org.splitties.compose.oclock.sample.extensions.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextMeasurer

/**
 * This value should reflect the default cache size for TextMeasurer.
 */
private val DefaultCacheSize: Int = 8

/**
 * Creates and remembers a [TextMeasurer]. All parameters that are required for [TextMeasurer]
 * except [cacheSize] are read from CompositionLocals. Created [TextMeasurer] carries an internal
 * [androidx.compose.ui.text.TextLayoutCache] with [cacheSize] capacity. Provide 0 for [cacheSize] to opt-out from internal
 * caching behavior.
 *
 * @param cacheSize Capacity of internal cache inside [TextMeasurer]. Size unit is the number of
 * unique text layout inputs that are measured. Value of this parameter highly depends on the
 * consumer use case. Provide a cache size that is in line with how many distinct text layouts are
 * going to be calculated by this measurer repeatedly. If you are animating font attributes, or any
 * other layout affecting input, cache can be skipped because most repeated measure calls would miss
 * the cache.
 */
@Composable
fun rememberTextOnPathMeasurer(
    cacheSize: Int = DefaultCacheSize
): TextOnPathMeasurer {
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current

    return remember(fontFamilyResolver, density, layoutDirection, cacheSize) {
        TextOnPathMeasurer(fontFamilyResolver, density, layoutDirection, cacheSize)
    }
}
