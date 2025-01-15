package ru.ikrom.album

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.toMediaItem
import ru.ikrom.ui.base_adapter.delegates.toMediaItems
import ru.ikrom.ui.models.toThumbnailHeaderItem
import ru.ikrom.ui.models.toThumbnailSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.AlbumModel
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
) : ViewModel() {
    private val _uiState = MutableLiveData<AlbumUiState>()
    val uiState: LiveData<AlbumUiState> = _uiState

    private var albumModel: AlbumModel? = null

    fun updateAlbum(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getAlbumPage(id)
            }.onSuccess { page ->
                albumModel = page.albumInfo
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
        (uiState.value as? AlbumUiState.Success)?.let {
            playerHandler.playNow(it.tracks.toMediaItems())
        }
    }

    fun playShuffled(){
        (uiState.value as? AlbumUiState.Success)?.let {
            playerHandler.playShuffle(it.tracks.toMediaItems())
        }
    }

    fun playTrack(track: ThumbnailItem){
        playerHandler.playNow(track.toMediaItem())
    }

    fun getArtistId(): String? {
        return albumModel?.artists?.first()?.id
    }

    companion object {
        val TAG = AlbumViewModel::class.simpleName
    }
}