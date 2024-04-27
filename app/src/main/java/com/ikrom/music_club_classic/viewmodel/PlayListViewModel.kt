package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.data.model.Playlist
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(
    val repository: MediaRepository
) : ViewModel() {
    val playlistItems = MutableLiveData<List<Track>>()

    fun updatePlaylistItems(id: String) {
        viewModelScope.launch {
            repository.getPlaylistTracks(id).asFlow().collect{
                playlistItems.postValue(it)
            }
        }
    }
}