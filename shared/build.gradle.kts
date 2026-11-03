import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

val generatedCredentialsDir = layout.buildDirectory.dir("generated/credentials/commonMain")

// Generates PaypalCredentials from local.properties so secrets stay out of version control.
val generatePaypalCredentials =
    tasks.register("generatePaypalCredentials") {
        val localProperties = rootProject.layout.projectDirectory.file("local.properties")
        val outputDir = generatedCredentialsDir
        inputs.files(localProperties).optional()
        outputs.dir(outputDir)
        doLast {
            val properties = Properties()
            val file = localProperties.asFile
            if (file.exists()) file.inputStream().use(properties::load)
            val clientId = properties.getProperty("paypal.clientId").orEmpty()
            val secretId = properties.getProperty("paypal.secretId").orEmpty()
            val target = outputDir.get().asFile.resolve("com/tylerdev/vitaforge/shared/PaypalCredentials.kt")
            target.parentFile.mkdirs()
            target.writeText(
                """
                package com.tylerdev.vitaforge.shared

                object PaypalCredentials {
                    const val CLIENT_ID = "$clientId"
                    const val SECRET_ID = "$secretId"
                }
                """.trimIndent() + "\n",
            )
        }
    }

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    android {
        namespace = "com.tylerdev.vitaforge.shared"
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()
        minSdk =
            libs.versions.android.minSdk
                .get()
                .toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.android.client)
        }

        iosMain.dependencies {
            implementation(libs.ktor.darwin.client)
        }
        commonMain {
            kotlin.srcDir(generatePaypalCredentials.map { generatedCredentialsDir })
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.kotlinx.serialization)
            implementation(libs.coil3)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.compose.core)
            implementation(libs.coil3.network.ktor)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}

compose.resources {
    publicResClass = true
}
