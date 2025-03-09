package ru.ikrom.player_repository

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlayerCacheScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DownloadCacheScope

@Module
@OptIn(UnstableApi::class)
@InstallIn(SingletonComponent::class)
class PlayerRepositoryModule {
    @Provides
    @Singleton
    internal fun provideDefaultDataSourceFactory(
        @ApplicationContext context: Context,
    ): DefaultDataSource.Factory {
        val okHttpFactory = OkHttpDataSource.Factory(
            OkHttpClient.Builder()
                .proxy(YTPlayerUtils.proxy)
                .build()
        )
        return DefaultDataSource.Factory(context, okHttpFactory)
    }

    @Provides
    @Singleton
    @PlayerCacheScope
    @OptIn(UnstableApi::class)
    internal fun providePlayerCache(@ApplicationContext context: Context, databaseProvider: DatabaseProvider)
            = SimpleCache(
        context.filesDir.resolve("player"),
        NoOpCacheEvictor(),
        databaseProvider)

    @Provides
    @Singleton
    @DownloadCacheScope
    @OptIn(UnstableApi::class)
    internal fun provideDownloadCache(@ApplicationContext context: Context, databaseProvider: DatabaseProvider)
            = SimpleCache(
        context.filesDir.resolve("download"),
        NoOpCacheEvictor(),
        databaseProvider)

    @Provides
    @Singleton
    @OptIn(UnstableApi::class)
    internal fun provideDatabaseProvider(@ApplicationContext context: Context): DatabaseProvider =
        StandaloneDatabaseProvider(context)

    @Provides
    @Singleton
    fun providePlayerRepository(
        @DownloadCacheScope downloadCache: SimpleCache,
        @PlayerCacheScope playerCache: SimpleCache,
        cacheDataSourceFactory: DefaultDataSource.Factory
    ): IPlayerRepository = DefaultPlayerRepository(downloadCache, playerCache, cacheDataSourceFactory)
}