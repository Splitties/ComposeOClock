package org.splitties.compose.oclock.sample.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toIntSize
import androidx.compose.ui.unit.toSize

fun TextLayoutResult.sizeForLayer(): IntSize {
    val spaceForBlur = (layoutInput.style.shadow?.blurRadius ?: 0f) * 2
    return (size.toSize() + spaceForBlur).toIntSize()
}

fun TextLayoutResult.blurOffset(): Offset = layoutInput.style.shadow?.blurRadius?.let {
    Offset(it, it)
} ?: Offset.Zero
