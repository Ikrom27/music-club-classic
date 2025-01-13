package ru.ikrom.library

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemMediumItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import ru.ikrom.youtube_data.model.getNames
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
): DefaultStateViewModel<UiState>() {
    private val tracks = mutableListOf<TrackModel>()

    override fun loadState() {
        _state.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getLikedTracks()

            }.onSuccess { result ->
                tracks.addAll(result)
                _state.postValue(UiState.Success(
                    result.map {
                        ThumbnailItemMediumItem(
                            id = it.videoId,
                            title = it.title,
                            subtitle = it.album.artists.getNames(),
                            thumbnail = it.album.thumbnail
                        )
                    }
                ))
            }.onFailure {
                _state.postValue(UiState.Error)
            }
        }
    }

    fun playTrackById(id: String) {
        tracks.firstOrNull() { it.videoId == id }?.let { playerHandler.playNow(it) }
    }
}
