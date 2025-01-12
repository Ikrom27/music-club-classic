package ru.ikrom.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.ui.models.toCardItem
import ru.ikrom.ui.models.toThumbnailMediumItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository,
): ViewModel() {
    private val _state = MutableLiveData<UiState>()
    private val tracksMap = mutableMapOf<String, TrackModel>()
    val state: LiveData<UiState> = _state

    init {
        update()
    }

    fun update(){
        _state.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                tracksMap.clear()
                val quickPickTracks = repository.getRadioTracks("Linkin park").map {
                    tracksMap[it.videoId] = it
                    it.toThumbnailMediumItem()
                }
                val favoriteTracks = repository.getLikedTracks().map {
                    tracksMap[it.videoId] = it
                    it.toThumbnailMediumItem()
                }
                val playlists = repository.getNewReleases().map { it.toCardItem() }
                UiState.Success(
                    quickPickTracks = quickPickTracks,
                    favoriteTracks = favoriteTracks,
                    playlists = playlists)
            }.onSuccess { successState ->
                _state.postValue(successState)
            }.onFailure {
                _state.postValue(UiState.Error)
            }
        }
    }

    fun playTrackById(id: String){
        tracksMap[id]?.let { playerHandler.playNow(it) }
    }
}