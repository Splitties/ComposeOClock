package com.louiscad.composeoclockplayground.benchmark

import android.app.Instrumentation
import android.app.UiAutomation
import android.content.ComponentName
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.Before
import org.junit.Rule
import org.junit.Test

abstract class WatchFaceBenchmark {
    private lateinit var instrumentation: Instrumentation
    private lateinit var uiAutomation: UiAutomation
    private lateinit var device: UiDevice

    abstract val watchfaceComponent: ComponentName

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Before
    fun setup() {
        instrumentation = InstrumentationRegistry.getInstrumentation()
        uiAutomation = instrumentation.uiAutomation
        device = UiDevice.getInstance(instrumentation)
    }

    @Before
    fun after() {
        device.wakeUp()
        device.startWatchface(DefaultWatchFace)
    }

    @Test
    fun oneMinuteInteractive() = benchmarkRule.measureRepeated(
        packageName = watchfaceComponent.packageName,
        metrics = metrics(),
        compilationMode = CompilationMode.Partial(),
        // TODO bump to multiple
        iterations = 1,
        startupMode = StartupMode.WARM,
        setupBlock = {
            device.startWatchface(watchfaceComponent)
            repeat(5) {
                println("Sleep in setupBlock $it")
                Thread.sleep(1000)
            }

            println("Waking Up")
            device.wakeUp()
        },
    ) {
        repeat(10) {
            device.wakeUp()
            println("Sleep in test $it")
            Thread.sleep(6000)
        }
    }

    // TODO add interesting watchface metrics
    open fun metrics() = listOf(IgnoredMetric)

    companion object {
        val PACKAGE_NAME = "com.louiscad.composeoclockplayground"
    }
}


