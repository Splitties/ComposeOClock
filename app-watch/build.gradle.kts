plugins {
    id("android-app")
    id("version-code-watch")
}

android {
    namespace = "com.louiscad.composeoclockplayground"

    defaultConfig {
        applicationId = "com.louiscad.composeoclockplayground"
        minSdk = 26
        targetSdk = 33
        versionName = version.toString()
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    coreLibraryDesugaring(Android.tools.desugarJdkLibs)
    implementation {
        project(":shared")()
        libs.compose.oclock.watchface.renderer()
        AndroidX.wear.watchFace.editor()

        platform(AndroidX.compose.bom)
        AndroidX.wear.compose.material()
        AndroidX.wear.compose.foundation()
        AndroidX.activity.compose()
        AndroidX.core.splashscreen()

        Splitties.systemservices()
        Splitties.toast()
    }
    androidTestImplementation {
        platform(AndroidX.compose.bom)
        AndroidX.compose.ui.testJunit4()
    }
    debugImplementation {
        platform(AndroidX.compose.bom)
        AndroidX.compose.ui.tooling()
        AndroidX.compose.ui.testManifest()
    }
}
