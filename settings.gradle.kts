pluginManagement {
    includeBuild("convention-plugins")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.60.4"
    id("org.splitties.settings-include-dsl") version "0.2.6"
    id("org.splitties.dependencies-dsl") version "0.2.0"
    id("org.splitties.version-sync") version "0.2.6"
}

run { // Remove when https://github.com/gradle/gradle/issues/2534 is fixed.
    val rootProjectPropertiesFile = rootDir.resolve("gradle.properties")
    val includedBuildPropertiesFile = rootDir.resolve("convention-plugins").resolve("gradle.properties")
    if (includedBuildPropertiesFile.exists().not() ||
        rootProjectPropertiesFile.readText() != includedBuildPropertiesFile.readText()
    ) {
        rootProjectPropertiesFile.copyTo(target = includedBuildPropertiesFile, overwrite = true)
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

rootProject.name = "ComposeOClock sample"

include {
    "app-phone"()
    "app-watch"()
    "shared"()
}
