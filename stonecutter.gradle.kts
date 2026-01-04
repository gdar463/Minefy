plugins {
    base
    id("dev.kikugie.stonecutter")
    id("fabric-loom") version "1.13-SNAPSHOT" apply false
}
stonecutter active file("versions/current")

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.isxander.dev/releases")
        maven("https://maven.terraformersmc.com/")
    }
}

tasks.clean {
    delete(layout.buildDirectory.dir("finalJars"))
}