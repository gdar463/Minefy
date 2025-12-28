plugins {
    id("dev.isxander.modstitch.base") version "0.7.1-unstable"
    id("it.nicolasfarabegoli.conventional-commits") version "3.1.3"
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val minecraft = property("deps.minecraft") as String;

conventionalCommits {
    successMessage = null
    failureMessage =
        "The commit message does not meet the Conventional Commits stantard. Check out CONTRIBUTING.md for more information."
}

modstitch {
    minecraftVersion = minecraft
    javaVersion = 21

    // If parchment doesn't exist for a version, yet you can safely
    // omit the "deps.parchment" property from your versioned gradle.properties
    parchment {
        prop("deps.parchment") { mappingsVersion = it }
    }

    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        modId = "minefy"
        modName = "Minefy"
        modVersion = "0.2.1"
        modGroup = "com.gdar463.minefy"
        modAuthor = "gdar463"

        val props = mapOf(
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            "mod_license" to "GPL-3.0-only",
            "mod_issue_tracker" to "https://github.com/gdar463/minefy/issues",
            "pack_format" to when (property("deps.minecraft")) {
                "1.21.1" -> 34
                "1.21.10" -> 69.0
                else -> throw IllegalArgumentException(
                    "Please store the resource pack version for ${
                        property(
                            "deps.minecraft"
                        )
                    } in build.gradle.kts! https://minecraft.wiki/w/Pack_format"
                )
            }.toString()
        )

        replacementProperties.set(props)
    }

    // Fabric Loom (Fabric)
    loom {
        // It's not recommended to store the Fabric Loader version in properties.
        // Make sure it's up to date.
        fabricLoaderVersion = "0.18.4"

        // Configure loom like normal in this block.
        configureLoom {

        }
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        prop("deps.neoforge") { neoForgeVersion = it }

        // Configures client and server runs for MDG, it is not done by default
        defaultRuns()

        // This block configures the `neoforge` extension that MDG exposes by default,
        // you can configure MDG like normal from here
        configureNeoForge {
            runs.all {
                disableIdeRun()
            }
        }
    }

    mixin {
        // You do not need to specify mixins in any mods.json/toml file if this is set to
        // true, it will automatically be generated.
        addMixinsToModManifest = true

        configs.register("minefy")
        if (isLoom) configs.register("minefy-fabric")
//        if (isModDevGradleRegular) configs.register("minefy-neoforge")
    }
}

// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
var constraint: String = name.split("-")[1]
stonecutter {
    constants {
        put("fabric", modstitch.isLoom)
        put("neoforge", modstitch.isModDevGradleRegular)
        put("forge", modstitch.isModDevGradleLegacy)
        put("forgelike", modstitch.isModDevGradle)
    }
}

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    /// Fabric
    if (modstitch.isLoom) {
        // FabricAPI
        modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

        // ModMenu
        modstitchModImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")
    }

    /// All Platforms
    // MCDev Annotations for Idea
    modstitchModCompileOnly("com.demonwav.mcdev:annotations:${property("deps.mcdev_annotations")}")

    // YACL
    modstitchModImplementation("dev.isxander:yet-another-config-lib:${property("deps.yacl")}")
}