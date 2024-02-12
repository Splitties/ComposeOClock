package org.splitties.compose.oclock.sample.extensions

import androidx.compose.ui.graphics.Paint

fun Paint.setFrom(other: Paint) {
    asFrameworkPaint().set(other.asFrameworkPaint())
}
