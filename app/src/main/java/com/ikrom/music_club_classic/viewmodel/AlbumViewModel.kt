package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    val repository: MediaRepository
) : ViewModel() {
    val currentAlbum= MutableLiveData<Album>()
    val albumTracks = MutableLiveData<List<Track>>()

    fun setAlbum(album: Album){
        currentAlbum.postValue(album)
        updateAlbumTracks(album.id)
    }

    fun updateAlbumTracks(id: String) {
        viewModelScope.launch {
            repository.getAlbumTracks(id).asFlow().collect{
                albumTracks.postValue(it)
            }
        }
    }
}