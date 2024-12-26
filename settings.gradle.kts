pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.google.com/") }
        maven("https://jetbrains.bintray.com/compose")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven { url = uri("https://maven.google.com/") }
        mavenCentral()
        maven("https://jetbrains.bintray.com/compose")
    }
}

rootProject.name = "Tranchat"
include(":app")