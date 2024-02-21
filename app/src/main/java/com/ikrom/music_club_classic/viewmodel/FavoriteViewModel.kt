package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MusicServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: MusicServiceRepository
) : ViewModel() {
    private val favoriteTrackLiveData = MutableLiveData<List<Track>>(emptyList())

    fun getTracks(): LiveData<List<Track>>{
        viewModelScope.launch {
            favoriteTrackLiveData.postValue(repository.getFavoriteTracks())
        }
        return favoriteTrackLiveData
    }
}