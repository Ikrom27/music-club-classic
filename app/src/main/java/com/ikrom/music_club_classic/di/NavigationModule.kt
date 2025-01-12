package com.ikrom.music_club_classic.di

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ikrom.music_club_classic.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.ikrom.album.AlbumFragment
import ru.ikrom.artist.ArtistFragment
import ru.ikrom.explore.ExploreFragment
import ru.ikrom.home.HomeFragment
import ru.ikrom.menu_track.TrackMenuFragment
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.toBundle
import ru.ikrom.ui.utils.idToBundle


@Module
@InstallIn(ActivityComponent::class)
class NavigationModule {
    @Provides
    fun provideNavController(activity: Activity): NavController {
        val navHostFragment = (activity as AppCompatActivity).supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        return navHostFragment?.navController ?: throw IllegalStateException("NavHostFragment not found")
    }

    @Provides
    fun provideAlbumNavigator(
        navController: NavController
    ) = object : AlbumFragment.Navigator {
        override fun toArtist(artistId: String) {
            navController.navigate(R.id.to_artist, idToBundle(artistId))
        }

        override fun toTrackMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }

    }

    @Provides
    fun provideExploreNavigator(
        navController: NavController
    ) = object : ExploreFragment.Navigator {
        override fun toSearchScreen() {
            navController.navigate(R.id.to_search)
        }

        override fun toAlbumScreen(albumId: String) {
            navController.navigate(R.id.to_album, idToBundle(albumId))
        }

        override fun toAlbumMenu(
            item: ThumbnailItem
        ) {
            navController.navigate(R.id.to_album, idToBundle(item.id))
        }

    }

    @Provides
    fun provideArtistNavigator(
        navController: NavController
    ) = object : ArtistFragment.Navigator{
        override fun toArtist(artistId: String) {
            navController.navigate(R.id.to_artist, idToBundle(artistId))
        }

        override fun toAlbum(albumId: String) {
            navController.navigate(R.id.to_album, idToBundle(albumId))
        }

        override fun toAlbumMenu(info: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track)
        }
    }

    @Provides
    fun provideHomeNavigator(
        navController: NavController
    ) = object : HomeFragment.Navigator {
        override fun toTrackMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }

        override fun toPlaylistMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }

        override fun toPlaylist(id: String) {
            navController.navigate(R.id.to_album, idToBundle(id))
        }

    }

    @Provides
    fun provideTrackMenuNavigator(
        navController: NavController
    ) = object : TrackMenuFragment.Navigator{
        override fun toAlbum(albumId: String) {
            navController.navigate(R.id.to_album, idToBundle(albumId))
        }

        override fun toArtist(artistId: String) {
            navController.navigate(R.id.to_artist, idToBundle(artistId))
        }

    }
}
