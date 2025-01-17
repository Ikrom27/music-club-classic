package com.ikrom.music_club_classic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
): ViewModel() {
    val totalDurationLiveData = playerHandler.totalDurationLiveData
    var bottomSheetState = BottomSheetBehavior.STATE_COLLAPSED
    val currentMediaItem = playerHandler.currentMediaItemLiveData
    val isPlaying = playerHandler.isPlayingLiveData
    val progressLiveData = playerHandler.currentPositionFlow.asLiveData()

    init {
        playerHandler.setOnTrackChanged { mediaItems, pos ->
            if (pos >= mediaItems.lastIndex){
                viewModelScope.launch(Dispatchers.IO) {
                    runCatching {
                       repository.getRadioTracks(mediaItems.last().mediaId).map { it.toMediaItem() }
                    }.onSuccess { tracks ->
                        val queueIds =  mediaItems.map { it.mediaId }
                        viewModelScope.launch(Dispatchers.Main){
                            playerHandler.addToQueue(tracks.filter { it.mediaId !in queueIds })
                        }
                    }.onFailure { e ->
                        Log.e(TAG, e.message.toString())
                    }
                }
            }
        }
    }

    fun togglePlayPause() {
        playerHandler.togglePlayPause()
    }

    companion object {
        val TAG = MainViewModel::class.simpleName
    }
}