package ru.ikrom.player_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: IMediaRepository,
    private val playerHandler: IPlayerHandler
): ViewModel() {
    private val _isFavoriteLiveData = MutableLiveData(false)
    val isFavoriteLiveData: LiveData<Boolean> = _isFavoriteLiveData
    val isPlayingLiveData = playerHandler.isPlayingLiveData
    val repeatModeLiveData = playerHandler.repeatModeLiveData
    val currentMediaItemLiveData = playerHandler.currentMediaItemLiveData
    val totalDurationLiveData = playerHandler.totalDurationLiveData
    var currentPositionLiveData = playerHandler.currentPositionFlow.asLiveData()

    fun updateIsFavoriteState(){
        currentMediaItemLiveData.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                _isFavoriteLiveData.postValue(repository.isFavorite(it.mediaId))
            }
        }
    }

    fun seekTo(position: Long){
        playerHandler.seekTo(position)
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

    fun handleToFavorite(){
        currentMediaItemLiveData.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                if(!repository.isFavorite(it.mediaId)) {
                    _isFavoriteLiveData.postValue(true)
                    repository.saveTrack(it.mediaId)
                } else {

                }
            }
        }
    }
}