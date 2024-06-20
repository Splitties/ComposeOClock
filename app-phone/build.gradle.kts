plugins {
    id("android-app")
    id("version-code-phone")
}

android {
    namespace = "com.louiscad.composeoclockplayground"

    defaultConfig {
        applicationId = "com.louiscad.composeoclockplayground"
        minSdk = 26
        targetSdk = 34
        versionName = version.toString()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    implementation {
        project(":shared")()

        AndroidX.compose.material3()
        AndroidX.compose.ui.toolingPreview()
        AndroidX.activity.compose()
        AndroidX.compose.ui()
        AndroidX.compose.ui.graphics()
        AndroidX.compose.ui.toolingPreview()
        AndroidX.compose.material3()
    }
    coreLibraryDesugaring(Android.tools.desugarJdkLibs)
    testImplementation {
        Testing.junit4()
    }
    androidTestImplementation {
        AndroidX.test.ext.junit()
        AndroidX.test.espresso.core()
        AndroidX.compose.ui.testJunit4()
    }
    debugImplementation {
        AndroidX.compose.ui.tooling()
        AndroidX.compose.ui.testManifest()
    }
}
