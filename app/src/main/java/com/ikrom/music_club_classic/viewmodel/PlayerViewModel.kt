package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: MediaRepository
): ViewModel() {
    val isFavoriteLiveData = MutableLiveData(false)

    fun isFavorite(id: String): MutableLiveData<Boolean> {
        viewModelScope.launch {
            isFavoriteLiveData.postValue(repository.isFavorite(id))
        }
        return isFavoriteLiveData
    }

    fun addToFavorite(mediaItem: MediaItem){
        repository.getTrackById(mediaItem.mediaId).observeForever {track->
            if (track != null) {
                viewModelScope.launch {
                    repository.addToFavorite(track)
                }
            }
        }
    }

    fun deleteTrackById(mediaItem: MediaItem){
        viewModelScope.launch {
            repository.deleteTrackById(mediaItem.mediaId)
        }
    }

    fun toggleToFavorite(mediaItem: MediaItem){
        isFavorite(mediaItem.mediaId).observeForever {
            if (it){
                deleteTrackById(mediaItem)
            } else {
                addToFavorite(mediaItem)
            }
        }
    }
}