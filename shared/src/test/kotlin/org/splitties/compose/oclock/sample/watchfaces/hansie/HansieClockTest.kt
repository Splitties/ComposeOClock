package org.splitties.compose.oclock.sample.watchfaces.hansie

import androidx.compose.runtime.Composable
import org.junit.Test
import org.robolectric.annotation.Config
import org.splitties.compose.oclock.sample.watchfaces.DeviceClockScreenshotTest
import org.splitties.compose.oclock.sample.watchfaces.WearDevice

class HansieClockTest(device: WearDevice) : DeviceClockScreenshotTest(device) {
    @Composable
    override fun Clock() {
        HansieClock()
    }

    @Config(qualifiers = "+fr-rFR")
    @Test
    fun french() = runTest {
        Clock()
    }

    @Config(qualifiers = "+ja")
    @Test
    fun japanese() = runTest {
        Clock()
    }
}