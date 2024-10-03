package com.ikrom.music_club_classic.di

import android.content.Context
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import com.ikrom.music_club_classic.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ikrom.explore.ExploreFragment
import ru.ikrom.search.SearchViewModel
import ru.ikrom.youtube_data.di.YoutubeModule
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Singleton


@Module(includes = [YoutubeModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    @OptIn(UnstableApi::class)
    fun provideDatabaseProvider(@ApplicationContext context: Context): DatabaseProvider =
        StandaloneDatabaseProvider(context)

    @Provides
    @Singleton
    fun provideSearchNavigation() = object : SearchViewModel.Navigate {
        override fun toBottomMenu(track: TrackModel) {}

        override fun navigateUp() {}
    }

    @Provides
    @Singleton
    fun provideExplorerDestinations(
        @ApplicationContext context: Context
    ) = object : ExploreFragment.Navigator {
        override fun toSearchScreenId(): Int {
            return R.id.explore_to_search
        }

        override fun toAlbumScreenId(): Int {
            return R.id.explore_to_album
        }

        override fun toAlbumMenu(id: String) {
            Toast.makeText(context, "Menu of ${id}", Toast.LENGTH_SHORT).show()
        }
    }
}
