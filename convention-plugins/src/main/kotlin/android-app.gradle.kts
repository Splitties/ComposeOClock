import de.fayard.refreshVersions.core.versionFor
import java.io.FileNotFoundException

plugins {
    kotlin("android")
    id("com.android.application")
}

android {
    compileSdk = 34
    defaultConfig {
        minSdk = 26
        targetSdk = 33
        resourceConfigurations += setOf("en", "fr")
    }
    // We embed the signing keystore to allow updating from another computer without uninstalling.
    val debugSigningConfig by signingConfigs.creating {
        storeFile = rootProject.file("debug.keystore")
        storePassword = "android"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
    }
    val keystoreFile = (findProperty("androidKeyFile") as String?)?.let { keyFilePath ->
        File(System.getProperty("user.home")).resolve(keyFilePath).also {
            if (it.exists().not()) throw FileNotFoundException("Didn't find keystore file at $it")
        }
    }
    val releaseSigningConfig = keystoreFile?.let {
        signingConfigs.create("releaseSigningConfig") {
            val androidKeyFile: String by project
            storeFile = File(System.getProperty("user.home")).resolve(androidKeyFile)
            val androidKeyAlias: String by project
            val androidKeyUniversalMdp: String by project
            storePassword = androidKeyUniversalMdp
            keyAlias = androidKeyAlias
            keyPassword = androidKeyUniversalMdp
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            signingConfig = debugSigningConfig
        }
        create("staging") {
            applicationIdSuffix = ".staging"
            matchingFallbacks += "release"
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            signingConfig = debugSigningConfig
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = releaseSigningConfig ?: debugSigningConfig
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kotlinOptions {
        freeCompilerArgs += "-opt-in=splitties.experimental.ExperimentalSplittiesApi"
        jvmTarget = "1.8"
        freeCompilerArgs += "-Xcontext-receivers"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = versionFor(AndroidX.compose.compiler)
    }
    packagingOptions.resources {
        excludes += setOf(
            "META-INF/ASL2.0",
            "META-INF/AL2.0",
            "META-INF/LGPL2.1",
            "META-INF/LICENSE",
            "META-INF/license.txt",
            "META-INF/NOTICE",
            "META-INF/notice.txt"
        )

        // Exclude files that unused kotlin-reflect would need, to make the app smaller:
        // (see issue https://youtrack.jetbrains.com/issue/KT-9770)
        excludes += setOf(
            "META-INF/*.kotlin_module",
            "kotlin/*.kotlin_builtins",
            "kotlin/**/*.kotlin_builtins"
        )
    }
}

dependencies {
    implementation {
        AndroidX.activity.compose()

        AndroidX.compose.runtime()
        AndroidX.compose.foundation()
    }
}
