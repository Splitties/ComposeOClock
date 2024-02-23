package org.splitties.compose.oclock.sample.watchfaces.hansie

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.captureRoboImage
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.splitties.compose.oclock.OClockRootCanvas

@Config(
    sdk = [33],
    qualifiers = "w227dp-h227dp-small-notlong-round-watch-xhdpi-keyshidden-nonav",
)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(AndroidJUnit4::class)
class HansieClockTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun roborazziTest() {
        composeRule.setContent {
            OClockRootCanvas(
                modifier = Modifier.fillMaxSize(),
                isAmbientFlow = MutableStateFlow(false)
            ) {
                HansieClock()
            }
        }

        composeRule
            .onRoot()
            .captureRoboImage()
    }
}