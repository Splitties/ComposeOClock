package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.runtime.Composable

class ComposeFanClockTest(device: WearDevice) : DeviceClockScreenshotTest(device) {
    @Composable
    override fun Clock() {
        ComposeFanClock()
    }
}