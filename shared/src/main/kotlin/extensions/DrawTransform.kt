@file:Suppress("NOTHING_TO_INLINE")

package org.splitties.compose.oclock.sample.extensions

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawTransform

inline fun DrawTransform.translate(offset: Offset) {
    translate(left = offset.x, top = offset.y)
}
