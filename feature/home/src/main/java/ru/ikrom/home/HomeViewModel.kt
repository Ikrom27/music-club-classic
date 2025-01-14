package ru.ikrom.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.ui.models.toCardItem
import ru.ikrom.ui.models.toGridItem
import ru.ikrom.ui.models.toThumbnailMediumItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository,
    private val quickPickUseCase: QuickPickUseCase
): DefaultStateViewModel<UiState>() {
    private val tracksMap = mutableMapOf<String, TrackModel>()

    init {
        likedTracksObserver()
    }

    companion object {
        val TAG = HomeViewModel::class.java.simpleName
    }

    override fun loadState() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                tracksMap.clear()
                val quickPickTracks = quickPickUseCase.getQuickPickTracks().map {
                    tracksMap[it.videoId] = it
                    it.toGridItem()
                }
                val favoriteTracks = repository.getLikedTracks().first().map {
                    tracksMap[it.videoId] = it
                    it.toThumbnailMediumItem()
                }
                val playlists = repository.getNewReleases().map { it.toCardItem() }
                UiState(
                    quickPickTracks = quickPickTracks,
                    favoriteTracks = favoriteTracks,
                    playlists = playlists)
            }.onSuccess { successState ->
                _state.postValue(successState)
            }.onFailure { e ->
                Log.e(TAG, e.message ?: "unknown")
            }
        }
    }

    private fun likedTracksObserver(){
        viewModelScope.launch {
            repository.getLikedTracks().collect { tracks ->
                _state.value?.apply {
                    _state.postValue(copy(favoriteTracks = tracks.map { it.toThumbnailMediumItem() }))
                }
            }
        }
    }

    fun playTrackById(id: String){
        tracksMap[id]?.let { playerHandler.playNow(it) }
    }
}