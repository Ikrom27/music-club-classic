package ru.ikrom.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.toMediaItem
import ru.ikrom.ui.models.toCardItem
import ru.ikrom.ui.models.toGridItem
import ru.ikrom.ui.models.toThumbnailMediumItem
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository,
    private val quickPickUseCase: QuickPickUseCase
): DefaultStateViewModel<UiState>() {

    init {
        loadState()
        likedTracksObserver()
    }

    companion object {
        val TAG = HomeViewModel::class.java.simpleName
    }

    private fun loadState() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val quickPickTracks = quickPickUseCase.getQuickPickTracks().map {
                    it.toGridItem() }
                val favoriteTracks = repository.getLikedTracks().first().map {
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

    fun playTrack(item: ThumbnailItem){
        playerHandler.playNow(item.toMediaItem())
    }
}