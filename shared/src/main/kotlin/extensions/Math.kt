package org.splitties.compose.oclock.sample.extensions

import kotlin.math.PI

fun Float.degreesToRadians(): Float = (PI / 180 * this).toFloat()
fun Float.radiansToDegrees(): Float = this * (180 / PI).toFloat()
