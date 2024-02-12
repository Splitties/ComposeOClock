package org.splitties.compose.oclock.sample.extensions

import kotlin.math.PI
import kotlin.math.sin

fun circleDiameterInCircularPattern(
    outerCircle: Float,
    n: Int
): Float {
    val a = sin(PI / n).toFloat()
    return (a * outerCircle) / (1 + a)
}

fun circleRadiusInCircularPattern(
    outerCircle: Float,
    n: Int
): Float = circleDiameterInCircularPattern(outerCircle, n) / 2f
