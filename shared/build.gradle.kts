import de.fayard.refreshVersions.core.versionFor

plugins {
    id("android-lib")
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    api {
        libs.compose.oclock.core()
        Google.android.playServices.wearOS()

        platform(AndroidX.compose.bom)
        AndroidX.compose.ui()
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
    }
    testImplementation {
        Testing.junit4()
    }
    androidTestImplementation {
        AndroidX.test.ext.junit.ktx()
        AndroidX.test.espresso.core()
    }
}
