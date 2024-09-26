package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.extensions.models.toThumbnailHeaderItem
import com.ikrom.music_club_classic.extensions.models.toThumbnailSmallItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallItem
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
    val albumTracks = MutableLiveData<List<ThumbnailSmallItem>>()
    val albumInfo = MutableLiveData<ThumbnailHeaderItem>()
    private val currentAlbumModel = MutableLiveData<AlbumModel>()
    private val albumsModelTracks = MutableLiveData<List<TrackModel>>()

    fun updateAlbum(id: String){
        viewModelScope.launch {
            val album = repository.getAlbumById(id)
            currentAlbumModel.postValue(album)
            albumInfo.postValue(album.toThumbnailHeaderItem())
        }
        updateAlbumTracks(id)
    }

    fun getTrackById(id: String): TrackModel? {
        return albumsModelTracks.value?.first { it.videoId == id }
    }

    fun getAllTracks(): List<TrackModel>{
        return albumsModelTracks.value ?: emptyList()
    }

    fun updateAlbumTracks(id: String) {
        viewModelScope.launch {
            val tracks = repository.getAlbumTracks(id).map { it }
            albumsModelTracks.postValue(tracks)
            albumTracks.postValue(tracks.map { it.toThumbnailSmallItem() })
        }
    }
}