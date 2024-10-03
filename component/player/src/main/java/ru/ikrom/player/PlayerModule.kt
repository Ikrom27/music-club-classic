package ru.ikrom.player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayerModule {
    @Provides
    @Singleton
    fun providePlayerHandler(
        player: ExoPlayer
    ): IPlayerHandler = PlayerHandlerImpl(player)

    @Provides
    @Singleton
    fun providePlayer(
        @ApplicationContext context: Context,
        mediaSourceFactory: DefaultMediaSourceFactory
    ) : ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(mediaSourceFactory)
        .build()

    @Provides
    @Singleton
    fun provideMusicPlayerService(): MusicPlayerService = MusicPlayerService()

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context,
        player: ExoPlayer
    ) : MusicNotificationManager = MusicNotificationManager(context, player)
}