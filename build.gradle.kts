import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    kotlin("jvm") version "1.9.23"

    application
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.7.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.gradleup.shadow") version "9.0.0-beta8"
}

group = "ru.luk.reelytras"
version = "1.2"

application {
    mainClass.set("$group.ReElytras")
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    paperweightDevelopmentBundle("io.papermc.paper:dev-bundle:1.20-R0.1-SNAPSHOT")
    implementation("com.jeff-media:MorePersistentDataTypes:2.4.0")
}

bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
    main = application.mainClass.get()
    apiVersion = "1.20"
    authors = listOf("_LuK__")

    commands.create("reelytras") {
        usage = "§cCorrect use: /reelytras [info/reload]"
        permission = "op"
    }
}


tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        enabled = true
    }

    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        @Suppress("deprecation", "RedundantSuppression")
        archiveFileName.set("ReElytras-$version.jar")
        relocate("com.jeff_media.morepersistentdatatypes", "ru.luk.reelytras.libs.morepersistentdatatypes")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}