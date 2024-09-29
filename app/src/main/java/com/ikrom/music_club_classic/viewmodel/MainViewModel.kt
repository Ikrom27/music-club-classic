package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ikrom.player.PlayerHandlerImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val playerHandler: PlayerHandlerImpl
): ViewModel() {
    fun togglePlayPause() {
        playerHandler.togglePlayPause()
    }

    var bottomSheetState = BottomSheetBehavior.STATE_COLLAPSED
    val currentMediaItem = playerHandler.currentMediaItemLiveData
    val isPlaying = playerHandler.isPlayingLiveData
}