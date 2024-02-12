package convention_plugins

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

enum class AppKind {
    Phone,
    Watch
}

fun Project.setVersionCodeForApp(kind: AppKind) {
    val android = the<BaseAppModuleExtension>()
    android.defaultConfig.versionCode = versionCode(kind)
}

private fun Project.versionCode(kind: AppKind): Int {
    val releaseBuildNumber = rootProject.layout.projectDirectory.file(
        "releaseBuildNumber.txt"
    ).asFile.useLines {
        it.first()
    }.toInt().also { require(it > 0) }
    return releaseBuildNumber * 2 - when (kind) {
        AppKind.Phone -> 1
        AppKind.Watch -> 0
    }
}
