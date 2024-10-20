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
include(":feature:search")
include(":feature:explore")
include(":component:player")
include(":component:searchbar")
include(":component:appbar")
include(":component:topbar")
include(":data:youtube")
include(":core:ui")
include(":core:theme")
include(":innertube")
include(":component:mini-player")
include(":feature:settings")
include(":core:utils")
include(":feature:library")
include(":feature:album")
include(":feature:artist")
include(":feature:home")
include(":feature:menu-track")
include(":feature:player-screen")
