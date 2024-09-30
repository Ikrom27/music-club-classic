pluginManagement {
    repositories {
        google()
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

rootProject.name = "Music Club Classic"
include(":app")
include(":innertube")
include(":data:youtube")
include(":core:ui")
include(":core:player")
include(":feature:search")
include(":core:searchbar")
