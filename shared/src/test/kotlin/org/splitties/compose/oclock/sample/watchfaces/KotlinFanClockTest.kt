package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.runtime.Composable

class KotlinFanClockTest(device: WearDevice) : DeviceClockScreenshotTest(device) {
    @Composable
    override fun Clock() {
        KotlinFanClock()
    }
}