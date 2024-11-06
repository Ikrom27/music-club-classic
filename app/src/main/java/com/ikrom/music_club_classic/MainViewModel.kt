package com.ikrom.music_club_classic

import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ikrom.player.PlayerHandlerImpl
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerHandler: PlayerHandlerImpl
): ViewModel() {
    val totalDurationLiveData = playerHandler.totalDurationLiveData
    var bottomSheetState = BottomSheetBehavior.STATE_COLLAPSED
    val currentMediaItem = playerHandler.currentMediaItemLiveData
    val isPlaying = playerHandler.isPlayingLiveData

    fun togglePlayPause() {
        playerHandler.togglePlayPause()
    }

    fun getPlayingProgress() = playerHandler.getCurrentPosition()
}