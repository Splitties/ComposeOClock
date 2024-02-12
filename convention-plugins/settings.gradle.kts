pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
    plugins {
        id("org.splitties.dependencies-dsl") version "0.1.0"
        id("de.fayard.refreshVersions") version "0.60.4"
    }
}

plugins {
    id("org.splitties.dependencies-dsl")
    id("de.fayard.refreshVersions")
}

refreshVersions {
    versionsPropertiesFile = rootDir.parentFile.resolve("versions.properties")
}
