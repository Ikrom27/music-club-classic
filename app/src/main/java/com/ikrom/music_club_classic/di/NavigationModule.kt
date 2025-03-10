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
import ru.ikrom.library.LibraryFragment
import ru.ikrom.library.favorite_tracks.FavoriteTracksFragment
import ru.ikrom.menu_artist.ArtistMenuFragment
import ru.ikrom.menu_track.TrackMenuFragment
import ru.ikrom.search.SearchFragment
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.base_adapter.toBundle
import ru.ikrom.library.favorite_albums.FavoriteAlbumsFragment
import ru.ikrom.library.favorite_artists.FavoriteArtistFragment
import ru.ikrom.library.favorite_playlists.FavoritePlaylistsFragment
import ru.ikrom.menu_album.AlbumMenuFragment
import ru.ikrom.playlist.PlaylistFragment
import ru.ikrom.utils.idToBundle


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

        override fun toUp() {
            navController.navigateUp()
        }

        override fun toAlbumMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_album, item.toBundle())
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
            navController.navigate(R.id.to_menu_album, item.toBundle())
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

        override fun toTrackMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }

        override fun toAlbumMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_album, item.toBundle())
        }

        override fun toArtistMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }
    }

    @Provides
    fun provideHomeNavigator(
        navController: NavController
    ) = object : HomeFragment.Navigator {
        override fun toTrackMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }

        override fun toSearchScreen() {
            navController.navigate(R.id.to_search)
        }

        override fun toPlaylistMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_album, item.toBundle())
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

    @Provides
    fun provideLibraryNavigator(
        navController: NavController
    ) = object : LibraryFragment.Navigator {
        override fun toTrackMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }

        override fun toFavoriteTracks() {
            navController.navigate(R.id.to_favorite_tracks)
        }

        override fun toFavoriteAlbums() {
            navController.navigate(R.id.to_favorite_albums)
        }

        override fun toPlaylists() {
            navController.navigate(R.id.to_favorite_playlists)
        }

        override fun toFavoriteArtists() {
            navController.navigate(R.id.to_favorite_artists)
        }

        override fun toLocalTracks() {
        }

    }


    @Provides
    fun provideFavoriteTracksNavigator(
        navController: NavController
    ) = object : FavoriteTracksFragment.Navigator {
        override fun toTrackMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }

    }

    @Provides
    fun provideFavoriteArtistFragment(
        navController: NavController
    ) = object : FavoriteArtistFragment.Navigator {
        override fun toArist(id: String) {
            navController.navigate(R.id.to_artist, idToBundle(id))
        }

        override fun toArtistMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_artist, item.toBundle())
        }
    }

    @Provides
    fun provideArtistMenuNavigator(
        navController: NavController
    ) = object : ArtistMenuFragment.Navigator {
        override fun toArtist(artistId: String) {
            navController.navigate(R.id.to_artist, idToBundle(artistId))
        }

    }

    @Provides
    fun provideSearchNavigator(
        navController: NavController
    ) = object : SearchFragment.Navigator {
        override fun toTrackMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }

        override fun toUp() {
            navController.navigateUp()
        }
    }

    @Provides
    fun provideAlbumMenuNavigator(
        navController: NavController
    ) = object : AlbumMenuFragment.Navigator {
        override fun toAlbum(id: String) {
            navController.navigate(R.id.to_album, idToBundle(id))
        }

        override fun toArtist(id: String) {
            navController.navigate(R.id.to_artist, idToBundle(id))
        }

    }

    @Provides
    fun provideFavoriteAlbumNavigator(
        navController: NavController
    ) = object : FavoriteAlbumsFragment.Navigator {
        override fun toAlbum(id: String) {
            navController.navigate(R.id.to_album, idToBundle(id))
        }

        override fun toAlbumMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_album, item.toBundle())
        }
    }

    @Provides
    fun provideFavoritePlaylistsNavigator(
        navController: NavController
    ) = object : FavoritePlaylistsFragment.Navigator {
        override fun toPlaylist(id: String) {
            navController.navigate(R.id.to_playlist, idToBundle(id))
        }
    }

    @Provides
    fun providePlaylistNavigator(
        navController: NavController
    ) = object : PlaylistFragment.Navigator {
        override fun toUp() {
            navController.navigateUp()
        }

        override fun toAlbumMenu(item: ThumbnailItem) {
            TODO("Not yet implemented")
        }

        override fun toTrackMenu(item: ThumbnailItem) {
            navController.navigate(R.id.to_menu_track, item.toBundle())
        }

    }
}
