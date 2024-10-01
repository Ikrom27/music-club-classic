package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toThumbnailHeaderItem
import ru.ikrom.ui.models.toThumbnailSmallItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailHeaderItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
) : ViewModel() {
    val albumTracks = MutableLiveData<List<ThumbnailSmallItem>>()
    val albumInfo = MutableLiveData<ThumbnailHeaderItem>()
    private val currentAlbumModel = MutableLiveData<AlbumModel>()
    private val albumsModelTracks = MutableLiveData<List<TrackModel>>()

    fun setAlbum(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val album = repository.getAlbumById(id)
                currentAlbumModel.postValue(album)
                albumInfo.postValue(album.toThumbnailHeaderItem())
            }.onFailure {
                println(it.message)
            }
        }
        updateAlbumTracks(id)
    }

    fun updateAlbumTracks(id: String) {
        viewModelScope.launch {
            runCatching {
                val tracks = repository.getAlbumTracks(id).map { it }
                albumsModelTracks.postValue(tracks)
                albumTracks.postValue(tracks.map { it.toThumbnailSmallItem() })
            }.onFailure {
                println(it.message)
            }
        }
    }

    fun playAllTracks(){
        albumsModelTracks.value?.let {
            playerHandler.playNow(it)
        }
    }

    fun playShuffled(){
        albumsModelTracks.value?.let {
            playerHandler.playNow(it.shuffled())
        }
    }

    fun playTrackById(id: String){
        playerHandler.playNow(getTrackById(id))
    }

    private fun getTrackById(id: String): TrackModel {
        return albumsModelTracks.value!!.first { it.videoId == id }
    }
}