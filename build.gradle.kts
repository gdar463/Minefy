plugins {
    id("dev.isxander.modstitch.base") version "0.7.1-unstable"
    id("it.nicolasfarabegoli.conventional-commits") version "3.1.3"
}

val id = m("id")
val v = m("version")
val minecraft = stonecutter.current.version
val loader = name.substringAfter("-")

/*
 All credits to the ChatPatches Contributers on GitHub
 Link: https://github.com/mrbuilder1961/ChatPatches/blob/0f6103578418203e059b157324de7f00a7a09244/build.gradle.kts

 All of the code until the next comment is under the LGPLv3.0 license
 available at: https://github.com/mrbuilder1961/ChatPatches/blob/0f6103578418203e059b157324de7f00a7a09244/LICENSE
*/
fun p(name: String, fallback: String? = null): String {
    val p = findProperty(name) as String?
    return when {
        p != null -> p
        fallback != null -> fallback
        else -> error("Property `$name` not set.")
    }
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    val p = p(name, "")
    if (p.isNotEmpty()) p.let(consumer)
}

fun d(name: String, fallback: String? = null): String = p("deps.$name", fallback)
fun dep(name: String, consumer: (prop: String) -> Unit) = prop("deps.$name", consumer)
fun m(name: String, fallback: String? = null): String = p("mod.$name", fallback)
/*
 Copied code ends here
*/

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
        dep("parchment") { mappingsVersion = it }
    }

    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        modId = id
        modName = m("name")
        modVersion = v
        modGroup = m("group")
        modAuthor = m("author")
        modLicense = m("license")

        val props = mapOf(
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            "mod_sources" to m("sources"),
            "mod_issue_tracker" to m("issue_tracker")
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
        dep("neoforge") { neoForgeVersion = it }

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

        configs.register(id)
        if (isLoom) configs.register("$id-fabric")
//        if (isModDevGradleRegular) configs.register("$id-neoforge")
    }
}

// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
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
        modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:${d("fabric_api")}")

        // ModMenu
        modstitchModImplementation("com.terraformersmc:modmenu:${d("modmenu")}")
    }

    /// All Platforms
    // MCDev Annotations for Idea
    modstitchModCompileOnly("com.demonwav.mcdev:annotations:${d("mcdev_annotations")}")

    // YACL
    modstitchModImplementation("dev.isxander:yet-another-config-lib:${d("yacl")}")
}

tasks {
    modstitch.finalJarTask {
        archiveBaseName.set(id)
        archiveVersion.set("$v+$minecraft")
        archiveClassifier.set(loader)
    }
}