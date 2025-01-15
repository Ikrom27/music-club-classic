package ru.ikrom.background_player

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.player_handler.PlayerHandlerImpl
import ru.ikrom.player_repository.IPlayerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PlayerModule {
    @Provides
    @Singleton
    fun providePlayerHandler(
        player: ExoPlayer,
    ): IPlayerHandler = PlayerHandlerImpl(player)

    @Provides
    @Singleton
    fun providePlayer(
        @ApplicationContext context: Context,
        playerRepository: IPlayerRepository
    ) : ExoPlayer = ExoPlayer.Builder(context)
        .setMediaSourceFactory(playerRepository.getMediaSourceFactory())
        .build()
}