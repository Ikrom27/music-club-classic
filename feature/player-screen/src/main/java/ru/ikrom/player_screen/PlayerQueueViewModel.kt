package ru.ikrom.player_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.player.PlayerHandlerImpl
import javax.inject.Inject

@HiltViewModel
class PlayerQueueViewModel @Inject constructor(
    playerHandler: IPlayerHandler
): ViewModel() {
    val currentMediaItem = playerHandler.currentMediaItemLiveData.value
    val currentQueue = playerHandler.currentQueue
    val recommendedQueue = playerHandler.recommendedQueue
}