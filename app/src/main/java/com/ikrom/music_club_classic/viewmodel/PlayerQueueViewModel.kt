package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ikrom.player.PlayerHandlerImpl
import javax.inject.Inject

@HiltViewModel
class PlayerQueueViewModel @Inject constructor(
    playerHandler: PlayerHandlerImpl
): ViewModel() {
    val currentMediaItem = playerHandler.currentMediaItemLiveData.value
    val currentQueue = playerHandler.currentQueue
    val recommendedQueue = playerHandler.recommendedQueue
}