pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PulsIq"
include(":app")
include(":core:ui")
include(":core:bluetooth")
include(":core:common")
include(":data:storage")
include(":feature:monitoring")
include(":feature:settings")
include(":feature:statistics")
include(":feature:pulseNotification")
