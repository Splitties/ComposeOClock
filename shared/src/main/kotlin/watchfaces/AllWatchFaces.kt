package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.runtime.Composable

val allWatchFaces: List<@Composable () -> Unit> = listOf(
    { KotlinFanClock() },
    { ComposeFanClock() },
    { BasicAnalogClock() },
)
