import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "navigation"
            isStatic = true
        }
    }

    android {
        namespace = "com.tylerdev.vitaforge.navigation"
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
            implementation(libs.compose.navigation)
            implementation(libs.kmpnotifier.push.firebase)
            implementation(project(path = ":feature:auth"))
            implementation(project(path = ":feature:home"))
            implementation(project(path = ":feature:home:cart:checkout"))
            implementation(project(path = ":feature:home:categories:category_search"))
            implementation(project(path = ":feature:profile"))
            implementation(project(path = ":feature:details"))
            implementation(project(path = ":feature:admin_panel"))
            implementation(project(path = ":feature:payment_completed"))
            implementation(project(path = ":feature:admin_panel:manage_product"))
            implementation(project(path = ":shared"))
            implementation(project(path = ":di"))
        }
        iosMain.dependencies {
            implementation(libs.auth.kmp)
            implementation(libs.firebase.app)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
