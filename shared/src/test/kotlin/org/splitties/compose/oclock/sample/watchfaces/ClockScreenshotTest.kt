@file:OptIn(ExperimentalRoborazziApi::class)

package org.splitties.compose.oclock.sample.watchfaces

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.ThresholdValidator
import com.github.takahirom.roborazzi.captureRoboImage
import com.louiscad.composeoclockplayground.shared.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assume
import org.junit.Assume.assumeFalse
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.splitties.compose.oclock.OClockRootCanvas
import java.io.File

@Config(
    sdk = [33],
    qualifiers = "w227dp-h227dp-small-notlong-round-watch-xhdpi-keyshidden-nonav",
)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
abstract class ClockScreenshotTest {
    @get:Rule(order = 0)
    val debugOnly = TestRule { base, _ ->
        Assume.assumeTrue("Screenshot tests not supported for release", BuildConfig.DEBUG)

        base
    }

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    abstract val device: WearDevice

    // generous to allow for mac/linux differences
    open val tolerance = 0.02f

    open val roborazziOptions: RoborazziOptions
        get() = RoborazziOptions(
            compareOptions = RoborazziOptions.CompareOptions(
                resultValidator = ThresholdValidator(tolerance)
            )
        )

    @Before
    fun check() {
        // https://github.com/robolectric/robolectric/issues/8312
        assumeFalse("Robolectric RNG not supported on Windows", System.getProperty("os.name")?.startsWith("Windows") ?: false)
    }

    fun runTest(isAmbient: Boolean = false, clock: @Composable () -> Unit) {
        val filePath =
            File("src/test/screenshots/${this.javaClass.simpleName}_${device.id}${if (isAmbient) "_ambient" else ""}.png")

        RuntimeEnvironment.setQualifiers("+w${device.dp}dp-h${device.dp}dp")

        composeRule.setContent {
            OClockRootCanvas(
                modifier = Modifier.fillMaxSize(), isAmbientFlow = MutableStateFlow(isAmbient)
            ) {
                clock()
            }
        }

        composeRule.onRoot()
            .captureRoboImage(filePath = filePath.path, roborazziOptions = roborazziOptions)
    }
}
