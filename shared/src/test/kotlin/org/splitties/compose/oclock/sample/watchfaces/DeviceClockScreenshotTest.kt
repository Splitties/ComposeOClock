package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.runtime.Composable
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.splitties.compose.oclock.sample.watchfaces.WearDevice.Companion.GooglePixelWatch
import org.splitties.compose.oclock.sample.watchfaces.WearDevice.Companion.MobvoiTicWatchPro5
import org.splitties.compose.oclock.sample.watchfaces.WearDevice.Companion.SamsungGalaxyWatch5

@RunWith(ParameterizedRobolectricTestRunner::class)
abstract class DeviceClockScreenshotTest(override val device: WearDevice): ClockScreenshotTest() {

    @Test
    fun interactive() = runTest {
        Clock()
    }

    @Test
    fun ambient() = runTest {
        Clock()
    }

    @Composable
    abstract fun Clock()

    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters
        fun devices() = listOf(
            MobvoiTicWatchPro5,
            SamsungGalaxyWatch5,
            GooglePixelWatch,
        )
    }
}