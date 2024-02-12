package org.splitties.compose.oclock.sample.extensions.text

import android.graphics.Typeface
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.TextLayoutInput
import androidx.compose.ui.text.TextLayoutResult

class TextOnPathLayoutResult(
    val internalResult: TextLayoutResult,
    typeFaceState: State<Typeface>
) {
    val layoutInput: TextLayoutInput get() = internalResult.layoutInput
    val typeface by typeFaceState
}
