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
    fun provideExplorerDestinations(
        navController: NavController
    ) = object : ExploreFragment.Navigator {
        override fun toSearchScreenId(): Int {
            return R.id.explore_to_search
        }

        override fun toAlbumScreenId(): Int {
            return R.id.to_album
        }

        override fun toAlbumMenu(id: String) {
            navController.navigate(R.id.to_album, bundleOf("id" to id))
        }
    }

    @Provides
    fun provideArtistNavigator() = object : ArtistFragment.Navigator{
        override val toArtistId: Int = R.id.artist_to_artist
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
