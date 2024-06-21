package org.splitties.compose.oclock.sample.utils

import androidx.annotation.IntRange

@JvmInline
value class FiveMinutesLayoutOrder(
    private val storage: Int
) {
    init {
        require((storage and 0b11111_00000_00000_00000).countOneBits() == 1)
        require((storage and 0b00000_11111_00000_00000).countOneBits() == 2)
        require((storage and 0b00000_00000_11111_00000).countOneBits() == 3)
        require((storage and 0b00000_00000_00000_11111).countOneBits() == 4)
    }

    companion object {
        val linear = FiveMinutesLayoutOrder(0b10000_11000_11100_11110)
        val symmetricalSpread = FiveMinutesLayoutOrder(0b00100_01010_10101_11011)
        val symmetricalPacked = FiveMinutesLayoutOrder(0b00100_01010_01110_11011)
        val symmetricalEdges = FiveMinutesLayoutOrder(0b00100_10001_10101_11011)
    }

    fun showFor(
        @IntRange(0, 59) minute: Int,
        @IntRange(0, 4) minuteMarkIndex: Int,
    ): Boolean {
        require(minute in 0..59)
        require(minuteMarkIndex in 0..4)
        return storage.getBitAt(
            position = ((minute - 1) % 5) * 5 + minuteMarkIndex,
            last = 19
        )
    }
}
