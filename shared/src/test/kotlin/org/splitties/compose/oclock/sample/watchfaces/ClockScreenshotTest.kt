package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assume
import org.junit.Assume.assumeFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.splitties.compose.oclock.OClockRootCanvas

@Config(
    sdk = [33],
    qualifiers = "w227dp-h227dp-small-notlong-round-watch-xhdpi-keyshidden-nonav",
)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
abstract class ClockScreenshotTest {
    @get:Rule
    val composeRule = createComposeRule()

    abstract val device: WearDevice

    @Before
    fun check() {
        // Robolectric RNG not supported on Windows
        // https://github.com/robolectric/robolectric/issues/8312
        assumeFalse(System.getProperty("os.name")?.startsWith("Windows") ?: false)
    }

    fun runTest(isAmbient: Boolean = false, clock: @Composable () -> Unit) {
        RuntimeEnvironment.setQualifiers("+w${device.dp}dp-h${device.dp}dp")

        composeRule.setContent {
            OClockRootCanvas(
                modifier = Modifier.fillMaxSize(), isAmbientFlow = MutableStateFlow(isAmbient)
            ) {
                clock()
            }
        }

        composeRule.onRoot()
            .captureRoboImage("src/test/screenshots/${this.javaClass.simpleName}_${device.id}${if (isAmbient) "_ambient" else ""}.png")
    }
}
