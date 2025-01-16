package ru.ikrom.youtube_data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ikrom.database.LocalDataSource
import ru.ikrom.youtube_data.IMediaDataSource
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.MediaRepositoryImpl
import ru.ikrom.youtube_data.YoutubeDataSource
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class YoutubeDataSourceScope

@Module
@InstallIn(SingletonComponent::class)
class YoutubeModule {
    @Provides
    @Singleton
    fun provideMediaRepository(
        @YoutubeDataSourceScope youtubeMusicDataSource: IMediaDataSource,
        localDataSource: LocalDataSource,
    ): IMediaRepository = MediaRepositoryImpl(youtubeMusicDataSource, localDataSource)

    @Provides
    @Singleton
    @YoutubeDataSourceScope
    internal fun provideYoutubeDataSource(): IMediaDataSource = YoutubeDataSource()
}