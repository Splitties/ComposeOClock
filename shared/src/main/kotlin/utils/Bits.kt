package org.splitties.compose.oclock.sample.utils

@PublishedApi
internal fun Int.getBitAt(position: Int, last: Int = 32): Boolean = this shr (last - position) and 1 > 0
