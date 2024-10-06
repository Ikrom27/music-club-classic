package com.ikrom.music_club_classic.di

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.findNavController
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


@Module()
@InstallIn(ActivityComponent::class)
class NavigationModule {

    @Provides
    fun provideNavController(activity: Activity): NavController {
        return activity.findNavController(R.id.nav_host_fragment)
    }

    @Provides
    fun provideAlbumNavigator(
        navController: NavController
    ) = object : AlbumFragment.Navigator {
        override fun toArtist(artistId: String) {
            navController.navigate(R.id.to_artist, bundleOf("id" to artistId))
        }

        override fun toTrackMenu(
            trackId: String,
            title: String,
            subtitle: String,
            thumbnail: String) {
            navController.navigate(
                R.id.to_menu_track,
                TrackMenuFragment.createBundle(
                    id = trackId,
                    title = title,
                    subtitle = subtitle,
                    thumbnail = thumbnail
                )
            )
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
            navController.navigate(R.id.to_album, AlbumFragment.createBundle(albumId))
        }

        override fun toAlbumMenu(
            albumId: String,
            title: String,
            subtitle: String,
            thumbnail: String
        ) {
            navController.navigate(R.id.to_album, AlbumFragment.createBundle(albumId))
        }

    }

    @Provides
    fun provideArtistNavigator() = object : ArtistFragment.Navigator{
        override val toArtistId: Int = R.id.to_artist
    }

    @Provides
    fun provideHomeNavigator() = object : HomeFragment.Navigator {
        override val menuTrackId: Int = R.id.to_menu_track

        override fun bundleMenuTrack(
            id: String,
            title: String,
            subtitle: String,
            thumbnail: String
        ): Bundle {
            return TrackMenuFragment.createBundle(id, title, subtitle, thumbnail)
        }
    }

    @Provides
    fun provideTrackMenuNavigator() = object : TrackMenuFragment.Navigator{
        override fun toAlbum(nanController: NavController, id: String) {

        }

        override fun toArtist(nanController: NavController, id: String) {

        }
    }
}
