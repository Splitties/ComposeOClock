plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
}


dependencies {
    fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"
    implementation {
        Android.tools.build.gradlePlugin()
        Kotlin.gradlePlugin()
        Google.playServicesGradlePlugin()
        Firebase.crashlyticsGradlePlugin()
        plugin("de.fayard.refreshVersions", "_")()
        plugin("org.splitties.dependencies-dsl", "_")()
    }
}
