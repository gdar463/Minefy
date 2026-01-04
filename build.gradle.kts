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

    parchment {
        dep("parchment") { mappingsVersion = it }
    }

    metadata {
        modId = id
        modName = m("name")
        modVersion = v
        modGroup = m("group")
        modAuthor = m("author")
        modLicense = m("license")

        val props = mapOf(
            "mod_sources" to m("sources"),
            "mod_issue_tracker" to m("issue_tracker")
        )

        replacementProperties.set(props)
    }

    // Fabric Loom (Fabric)
    loom {
        fabricLoaderVersion = "0.18.4"
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        dep("neoforge") { neoForgeVersion = it }

        defaultRuns()

        configureNeoForge {
            runs.all {
                disableIdeRun()
            }
        }
    }

    mixin {
        addMixinsToModManifest = true

        configs.register(id)
        if (isLoom) configs.register("$id-fabric")
    }
}

stonecutter {
    constants {
        put("fabric", modstitch.isLoom)
        put("neoforge", modstitch.isModDevGradleRegular)
        put("forge", modstitch.isModDevGradleLegacy)
        put("forgelike", modstitch.isModDevGradle)
    }
}

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

val finalJarTasks = listOf(modstitch.finalJarTask)

val buildAndCollect by tasks.registering(Copy::class) {
    finalJarTasks.forEach { jar ->
        dependsOn(jar)
        from(jar.flatMap { it.archiveFile })
    }

    into(rootProject.layout.buildDirectory.dir("finalJars"))
}

if (stonecutter.current.isActive) {
    tasks.register("buildAndCollectActive") {
        dependsOn(tasks.named("buildAndCollect"))
    }
}