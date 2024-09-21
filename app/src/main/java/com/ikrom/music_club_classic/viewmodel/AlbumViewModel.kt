package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    val repository: IMediaRepository
) : ViewModel() {
    val currentAlbum= MutableLiveData<AlbumModel>()
    val albumTracks = MutableLiveData<List<TrackModel>>()

    fun setAlbum(album: AlbumModel){
        currentAlbum.postValue(album)
        updateAlbumTracks(album.id)
    }

    fun updateAlbumTracks(id: String) {
        viewModelScope.launch {
            albumTracks.postValue(repository.getAlbumTracks(id).map { it })
        }
    }
}