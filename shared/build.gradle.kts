@file:Suppress("UnstableApiUsage")

plugins {
    id("android-lib")
    kotlin("plugin.compose")
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.louiscad.composeoclockplayground.shared"

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api {
        libs.compose.oclock.core()
        Google.android.playServices.wearOS()

        AndroidX.compose.ui()
        AndroidX.compose.ui.graphics()
        AndroidX.compose.ui.toolingPreview()
        AndroidX.compose.ui.text.googleFonts()

        AndroidX.core.ktx()
        AndroidX.lifecycle.runtime.ktx()
    }
    coreLibraryDesugaring(Android.tools.desugarJdkLibs)
    implementation {
        Splitties.systemservices()
        Splitties.appctx()
        Splitties.bitflags()
    }
    debugImplementation {
        AndroidX.compose.ui.tooling() //Important so previews can work.
        AndroidX.compose.ui.testManifest() // import for tests
    }
    testImplementation {
        Testing.junit4()
        Testing.robolectric()
        AndroidX.test.ext.junit.ktx()
        AndroidX.test.runner()
        AndroidX.compose.ui.testJunit4()
        libs.roborazzi()
        libs.roborazzi.compose()
        libs.roborazzi.rule()
    }
    androidTestImplementation {
        AndroidX.test.ext.junit.ktx()
        AndroidX.test.espresso.core()
    }
}
