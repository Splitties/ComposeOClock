import java.io.FileNotFoundException

plugins {
    id("com.android.application")
}

android {
    val debugSigningConfig by signingConfigs.creating {
        storeFile = rootProject.file("debug.keystore")
        storePassword = "android"
        keyAlias = "androiddebugkey"
        keyPassword = "android"
    }
    val keystoreFile = (property("androidKeyFile") as String?)?.let { keyFilePath ->
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
            signingConfig = debugSigningConfig
        }
        release {
            signingConfig = releaseSigningConfig ?: debugSigningConfig
        }
    }
}
