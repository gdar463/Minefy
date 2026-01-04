pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()

        // Modstitch
        maven("https://maven.isxander.dev/releases/")

        // Loom platform
        maven("https://maven.fabricmc.net/")

        // MDG platform
        maven("https://maven.neoforged.net/releases/")

        // Stonecutter
        maven("https://maven.kikugie.dev/releases")
        maven("https://maven.kikugie.dev/snapshots")

        // Modstitch
        maven("https://maven.isxander.dev/releases")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8.1"
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    val ciSingleBuild: String? = System.getenv("CI_SINGLE_BUILD")
    if (ciSingleBuild != null) {
        val split = ciSingleBuild.split(":")
        create(rootProject) {
            version(split[0], split[1])
        }
    } else {
        create(rootProject, file("versions/versions.json5"))
    }
}

rootProject.name = "Minefy"

