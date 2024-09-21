package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(
    val repository: IMediaRepository
) : ViewModel() {
    val playlistItems = MutableLiveData<List<TrackModel>>()

    fun updatePlaylistItems(id: String) {
        viewModelScope.launch {
            playlistItems.postValue(repository.getPlaylistTracks(id))
        }
    }
}