package com.ikrom.music_club_classic.di

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.ikrom.music_club_classic.data.data_source.SettingsDataSource
import com.ikrom.music_club_classic.data.data_source.account_data_source.AccountLocalDataSource
import com.ikrom.music_club_classic.data.repository.SettingsRepository
import com.ikrom.music_club_classic.utils.MediaSourceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlayerCacheScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DownloadCacheScope


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideMediaSourceFactory(
        @ApplicationContext context: Context,
        @DownloadCacheScope
        downloadCache: SimpleCache,
        @PlayerCacheScope
        playerCache: SimpleCache,
    ) : DefaultMediaSourceFactory
            = MediaSourceFactory(downloadCache, playerCache).createMediaSourceFactory(context)

    @Singleton
    @Provides
    @OptIn(UnstableApi::class)
    fun provideDatabaseProvider(@ApplicationContext context: Context): DatabaseProvider =
        StandaloneDatabaseProvider(context)

    @Provides
    @Singleton
    @PlayerCacheScope
    @OptIn(UnstableApi::class)
    fun providePlayerCache(@ApplicationContext context: Context, databaseProvider: DatabaseProvider)
            = SimpleCache(
        context.filesDir.resolve("player"),
        NoOpCacheEvictor(),
        databaseProvider)

    @Provides
    @Singleton
    @DownloadCacheScope
    @OptIn(UnstableApi::class)
    fun provideDownloadCache(@ApplicationContext context: Context, databaseProvider: DatabaseProvider)
            = SimpleCache(
        context.filesDir.resolve("download"),
        NoOpCacheEvictor(),
        databaseProvider)

    @Provides
    @Singleton
    fun provideAccountLocalDataSource(
        @ApplicationContext context: Context,
    ) : AccountLocalDataSource = AccountLocalDataSource(context)

    @Provides
    @Singleton
    fun provideSettingsDataSource(
        @ApplicationContext context: Context
    ) = SettingsDataSource(context)

    @Provides
    @Singleton
    fun provideSettingsRepository(
        settingsDataSource: SettingsDataSource
    ) = SettingsRepository(settingsDataSource)
}
