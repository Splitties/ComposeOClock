@file:Suppress("UnstableApiUsage")

plugins {
    kotlin("android")
    id("com.android.test")
}

android {
    namespace = "com.louiscad.composeoclockplayground.benchmark"

    defaultConfig {
        minSdk = 30
        compileSdk = 34
        targetSdk = 34

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.profiling.mode"] = "none"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        // This benchmark buildType is used for benchmarking, and should function like your
        // release build (for example, with minification on). It's signed with a debug key
        // for easy local/CI testing.
        val benchmark by creating {
            isDebuggable = false
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
        }
    }

    targetProjectPath = ":app-watch"
    experimentalProperties["android.experimental.self-instrumenting"] = true
}

dependencies {
    implementation {
        platform(AndroidX.compose.bom)
        AndroidX.compose.ui.testJunit4()
        AndroidX.test.runner()
        AndroidX.test.rules()
        AndroidX.test.uiAutomator()
        AndroidX.benchmark.macroJunit4()
    }
}

androidComponents {
    beforeVariants {
        it.enable = it.buildType == "benchmark"
    }
}
