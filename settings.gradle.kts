rootProject.name = "VitaForge"

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}
include(":data")
include(":di")
include(":feature:admin_panel")
include(":feature:admin_panel:manage_product")
include(":feature:home:products_overview")

include(":feature:profile")
include(":feature:home")
include(":feature:auth")
include(":navigation")
include(":androidApp")
include(":shared")
