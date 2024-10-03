package ru.ikrom.album

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toThumbnailSmallItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.ui.models.toThumbnailHeaderItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
) : ViewModel() {
    private val _uiState = MutableLiveData<AlbumUiState>()
    val uiState: LiveData<AlbumUiState> = _uiState

    private val albumsModelTracks = MutableLiveData<List<TrackModel>>()

    fun updateAlbum(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getAlbumPage(id)
            }.onSuccess { page ->
                _uiState.postValue(AlbumUiState.Success(
                    header = page.albumInfo.toThumbnailHeaderItem(),
                    tracks = page.tracks.map { it.toThumbnailSmallItem() }
                ))
            }.onFailure {e ->
                Log.e(TAG, e.message.toString())
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

    companion object {
        val TAG = AlbumViewModel::class.simpleName
    }
}