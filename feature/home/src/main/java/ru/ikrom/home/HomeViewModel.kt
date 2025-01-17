package ru.ikrom.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.delegates.CardItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemGrid
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemMediumItem
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.adapter_delegates.ext.toMediaItem
import ru.ikrom.adapter_delegates.modelsExt.toCardItem
import ru.ikrom.adapter_delegates.modelsExt.toGridItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailMediumItem
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
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

    private fun loadState() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.postValue(UiState.Loading)

            val quickPickTracks = runCatching {
                quickPickUseCase.getQuickPickTracks().map { it.toGridItem() }
            }.getOrElse {
                emptyList()
            }

            val favoriteTracks = runCatching {
                repository.getLikedTracks().firstOrNull()?.map { it.toThumbnailMediumItem() }
            }.getOrElse {
                emptyList()
            } ?: emptyList()

            val playlists = runCatching {
                repository.getNewReleases().map { it.toCardItem() }
            }.getOrElse {
                emptyList()
            }

            _state.postValue(
                UiState.Success(
                    quickPickTracks = quickPickTracks,
                    favoriteTracks = favoriteTracks,
                    playlists = playlists
                )
            )
        }
    }


    private fun likedTracksObserver(){
        viewModelScope.launch {
            repository.getLikedTracks().collect { tracks ->
                (_state.value as? UiState.Success)?.apply {
                    _state.postValue(copy(favoriteTracks = tracks.map { it.toThumbnailMediumItem() }))
                }
            }
        }
    }

    fun playTrack(item: ThumbnailItem){
        playerHandler.playNow(item.toMediaItem())
    }
}