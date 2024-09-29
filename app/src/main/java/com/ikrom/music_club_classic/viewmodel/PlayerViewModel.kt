package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: IMediaRepository,
    private val playerHandler: IPlayerHandler
): ViewModel() {
    val isFavoriteLiveData = MutableLiveData(false)
    val isPlayingLiveData = playerHandler.isPlayingLiveData
    val repeatModeLiveData = playerHandler.repeatModeLiveData
    val currentMediaItemLiveData = playerHandler.currentMediaItemLiveData
    val totalDurationLiveData = playerHandler.totalDurationLiveData
    var currentPositionLiveData = 0L
        get() {
            return playerHandler.getCurrentPosition()
        }

    fun seekTo(position: Long){
        playerHandler.seekTo(position)
    }

    fun isFavorite(id: String): MutableLiveData<Boolean> {
        viewModelScope.launch {
            isFavoriteLiveData.postValue(repository.isFavorite(id))
        }
        return isFavoriteLiveData
    }

    fun togglePlayPause() {
        playerHandler.togglePlayPause()
    }

    fun seekToNext() {
        playerHandler.seekToNext()
    }

    fun seekToPrevious() {
        playerHandler.seekToPrevious()
    }

    fun toggleRepeat() {
        playerHandler.toggleRepeat()
    }
}