package org.splitties.compose.oclock.sample.cleanthisbeforerelease.elements

inline fun circularRepeat(
    count: Int,
    block: (angleInDegrees: Float) -> Unit
) {
    repeat(count) { i ->
        block(360f * i / count)
    }
}
