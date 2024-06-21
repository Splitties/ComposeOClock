package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.runtime.Composable
import org.splitties.compose.oclock.sample.elements.LoopingSeconds

val allWatchFaces: List<@Composable () -> Unit> = listOf(
    { LoopingSeconds() },
    { LightLinesWatchFace() },
    { KotlinFanClock() },
    { ComposeFanClock() },
    { BasicAnalogClock() },
)
