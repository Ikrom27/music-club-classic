package ru.ikrom.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ikrom.database.db.AppDatabase
import ru.ikrom.database.db.TracksDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app-db"
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideTracksDao(
        db: AppDatabase
    ): TracksDao = db.likedTracksDao()

    @Provides
    @Singleton
    fun provideArtistDao(
        db: AppDatabase
    ) = db.likedArtistsDao()

    @Provides
    @Singleton
    fun provideAlbumDao(
        db: AppDatabase
    ) = db.likedAlbumDao()

    @Provides
    @Singleton
    fun providePlaylistDao(
        db: AppDatabase
    ) = db.playlistDao()
}