package ru.ikrom.background_player

import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.player_handler.PlayerHandlerImpl
import ru.ikrom.player_handler.utils.DataListenerRenderFactory
import ru.ikrom.player_handler.utils.IAudioDataListener
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

    @OptIn(UnstableApi::class)
    @Provides
    @Singleton
    fun providePlayer(
        @ApplicationContext context: Context,
        playerRepository: IPlayerRepository,
        audioListener: IAudioDataListener,
    ) : ExoPlayer = ExoPlayer.Builder(context)
        .setRenderersFactory(DataListenerRenderFactory(context, audioListener))
        .setMediaSourceFactory(playerRepository.getMediaSourceFactory())
        .build()
}