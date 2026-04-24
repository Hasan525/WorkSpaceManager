pluginManagement {
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "WorkspaceManager"
include(":app")

val localPropsFile = file("local.properties")
if (!localPropsFile.exists()) {
    val sdkPath = listOf(
        System.getenv("ANDROID_HOME"),
        System.getenv("ANDROID_SDK_ROOT"),
        System.getProperty("user.home")?.let { "$it/AppData/Local/Android/Sdk" },
        System.getProperty("user.home")?.let { "$it/Library/Android/sdk" },
        System.getProperty("user.home")?.let { "$it/Android/Sdk" }
    ).firstOrNull { !it.isNullOrBlank() && file(it).exists() }

    if (sdkPath != null) {
        localPropsFile.writeText("sdk.dir=${sdkPath.replace("\\", "\\\\").replace(":", "\\:")}\n")
        println("Auto-generated local.properties with sdk.dir=$sdkPath")
    }
}
