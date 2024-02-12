package org.splitties.compose.oclock.sample.extensions

import androidx.compose.ui.graphics.Color

fun List<Color>.loop(): List<Color> = this + this.first()
