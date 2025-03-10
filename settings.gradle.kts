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
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Music Club Classic"
include(":app")
include(":feature:search")
include(":feature:explore")
include(":common:player-handler")
include(":component:searchbar")
include(":component:appbar")
include(":component:topbar")
include(":data:youtube-repository")
include(":common:adapter-delegates")
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
include(":data:database")
include(":core:base-fragment")
include(":common:fragment-list-editable")
include(":feature:menu-artist")
include(":service:background-player")
include(":data:player-repository")
include(":core:base-adapter")
include(":component:placeholder")
include(":common:fragment-bottom-menu")
include(":feature:menu-album")
include(":feature:playlist")
