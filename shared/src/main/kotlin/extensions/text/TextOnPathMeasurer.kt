package org.splitties.compose.oclock.sample.extensions.text

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.resolveAsTypeface
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@Immutable
class TextOnPathMeasurer(
    private val defaultFontFamilyResolver: FontFamily.Resolver,
    private val defaultDensity: Density,
    private val defaultLayoutDirection: LayoutDirection,
    private val cacheSize: Int = 8
) {
    private val internalMeasurer = TextMeasurer(
        defaultFontFamilyResolver = defaultFontFamilyResolver,
        defaultDensity = defaultDensity,
        defaultLayoutDirection = defaultLayoutDirection,
        cacheSize = cacheSize
    )

    /**
     * Creates a [TextLayoutResult] according to given parameters.
     *
     * This function supports laying out text that consists of multiple paragraphs, includes
     * placeholders, wraps around soft line breaks, and might overflow outside the specified size.
     *
     * Most parameters for text affect the final text layout. One pixel change in [constraints]
     * boundaries can displace a word to another line which would cause a chain reaction that
     * completely changes how text is rendered.
     *
     * On the other hand, some attributes only play a role when drawing the created text layout.
     * For example text layout can be created completely in black color but we can apply
     * [TextStyle.color] later in draw phase. This also means that animating text color shouldn't
     * invalidate text layout.
     *
     * Thus, [textLayoutCache] helps in the process of converting a set of text layout inputs to
     * a text layout while ignoring non-layout-affecting attributes. Iterative calls that use the
     * same input parameters should benefit from substantial performance improvements.
     *
     * @param text the text to be laid out
     * @param style the [TextStyle] to be applied to the whole text
     * @param overflow How visual overflow should be handled.
     * @param softWrap Whether the text should break at soft line breaks. If false, the glyphs in
     * the text will be positioned as if there was unlimited horizontal space. If [softWrap] is
     * false, [overflow] and TextAlign may have unexpected effects.
     * @param maxLines An optional maximum number of lines for the text to span, wrapping if
     * necessary. If the text exceeds the given number of lines, it will be truncated according to
     * [overflow] and [softWrap]. If it is not null, then it must be greater than zero.
     * @param placeholders a list of [Placeholder]s that specify ranges of text which will be
     * skipped during layout and replaced with [Placeholder]. It's required that the range of each
     * [Placeholder] doesn't cross paragraph boundary, otherwise [IllegalArgumentException] is
     * thrown.
     * @param constraints how wide and tall the text is allowed to be. [Constraints.maxWidth]
     * will define the width of the MultiParagraph. [Constraints.maxHeight] helps defining the
     * number of lines that fit with ellipsis is true. [Constraints.minWidth] defines the minimum
     * width the resulting [TextLayoutResult.size] will report. [Constraints.minHeight] is no-op.
     * @param layoutDirection layout direction of the measurement environment. If not specified,
     * defaults to the value that was given during initialization of this [TextMeasurer].
     * @param density density of the measurement environment. If not specified, defaults to
     * the value that was given during initialization of this [TextMeasurer].
     * @param fontFamilyResolver to be used to load the font given in [SpanStyle]s. If not
     * specified, defaults to the value that was given during initialization of this [TextMeasurer].
     * @param skipCache Disables cache optimization if it is passed as true.
     *
     * @sample androidx.compose.ui.text.samples.measureTextAnnotatedString
     */
    @Stable
    fun measure(
        text: AnnotatedString,
        style: TextStyle = TextStyle.Default,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        placeholders: List<AnnotatedString.Range<Placeholder>> = emptyList(),
        constraints: Constraints = Constraints(),
        layoutDirection: LayoutDirection = this.defaultLayoutDirection,
        density: Density = this.defaultDensity,
        fontFamilyResolver: FontFamily.Resolver = this.defaultFontFamilyResolver,
        skipCache: Boolean = false
    ): TextOnPathLayoutResult {
        val internalResult = internalMeasurer.measure(
            text = text,
            style = style,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            placeholders = placeholders,
            constraints = constraints,
            layoutDirection = layoutDirection,
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            skipCache = skipCache
        )
        val typeFaceState = fontFamilyResolver.resolveAsTypeface(
            fontFamily = style.fontFamily,
            fontWeight = style.fontWeight ?: FontWeight.Normal,
            fontStyle = style.fontStyle ?: FontStyle.Normal,
            fontSynthesis = style.fontSynthesis ?: FontSynthesis.All
        )
        return TextOnPathLayoutResult(internalResult, typeFaceState)
    }

    /**
     * Creates a [TextLayoutResult] according to given parameters.
     *
     * This function supports laying out text that consists of multiple paragraphs, includes
     * placeholders, wraps around soft line breaks, and might overflow outside the specified size.
     *
     * Most parameters for text affect the final text layout. One pixel change in [constraints]
     * boundaries can displace a word to another line which would cause a chain reaction that
     * completely changes how text is rendered.
     *
     * On the other hand, some attributes only play a role when drawing the created text layout.
     * For example text layout can be created completely in black color but we can apply
     * [TextStyle.color] later in draw phase. This also means that animating text color shouldn't
     * invalidate text layout.
     *
     * Thus, [textLayoutCache] helps in the process of converting a set of text layout inputs to
     * a text layout while ignoring non-layout-affecting attributes. Iterative calls that use the
     * same input parameters should benefit from substantial performance improvements.
     *
     * @param text the text to be laid out
     * @param style the [TextStyle] to be applied to the whole text
     * @param overflow How visual overflow should be handled.
     * @param softWrap Whether the text should break at soft line breaks. If false, the glyphs in
     * the text will be positioned as if there was unlimited horizontal space. If [softWrap] is
     * false, [overflow] and TextAlign may have unexpected effects.
     * @param maxLines An optional maximum number of lines for the text to span, wrapping if
     * necessary. If the text exceeds the given number of lines, it will be truncated according to
     * [overflow] and [softWrap]. If it is not null, then it must be greater than zero.
     * @param constraints how wide and tall the text is allowed to be. [Constraints.maxWidth]
     * will define the width of the MultiParagraph. [Constraints.maxHeight] helps defining the
     * number of lines that fit with ellipsis is true. [Constraints.minWidth] defines the minimum
     * width the resulting [TextLayoutResult.size] will report. [Constraints.minHeight] is no-op.
     * @param layoutDirection layout direction of the measurement environment. If not specified,
     * defaults to the value that was given during initialization of this [TextMeasurer].
     * @param density density of the measurement environment. If not specified, defaults to
     * the value that was given during initialization of this [TextMeasurer].
     * @param fontFamilyResolver to be used to load the font given in [SpanStyle]s. If not
     * specified, defaults to the value that was given during initialization of this [TextMeasurer].
     * @param skipCache Disables cache optimization if it is passed as true.
     *
     * @sample androidx.compose.ui.text.samples.measureTextStringWithConstraints
     */
    @Stable
    fun measure(
        text: String,
        style: TextStyle = TextStyle.Default,
        overflow: TextOverflow = TextOverflow.Clip,
        softWrap: Boolean = true,
        maxLines: Int = Int.MAX_VALUE,
        constraints: Constraints = Constraints(),
        layoutDirection: LayoutDirection = this.defaultLayoutDirection,
        density: Density = this.defaultDensity,
        fontFamilyResolver: FontFamily.Resolver = this.defaultFontFamilyResolver,
        skipCache: Boolean = false
    ): TextOnPathLayoutResult {
        return measure(
            text = AnnotatedString(text),
            style = style,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            constraints = constraints,
            layoutDirection = layoutDirection,
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            skipCache = skipCache
        )
    }
}
