package ru.ikrom.artist


import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.ui.base_adapter.delegates.ArtistHeaderItem
import ru.ikrom.ui.base_adapter.delegates.toMediaItem
import ru.ikrom.ui.base_adapter.delegates.toMediaItems
import ru.ikrom.ui.models.toThumbnailLargeItem
import ru.ikrom.ui.models.toThumbnailMediumItem
import ru.ikrom.ui.models.toThumbnailRoundedItem
import ru.ikrom.ui.models.toThumbnailSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.ArtistModel
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
): DefaultStateViewModel<UiState>() {
    private var artistModel: ArtistModel? = null
    private var _isFavorite = false
    private var lastId = ""

    fun updateArtist(artistId: String = lastId){
        lastId = artistId
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getArtistData(artistId) to
                repository.isFavoriteArtist(artistId)
            }.onSuccess { (artistPage, isFavorite) ->
                artistModel = artistPage.toArtistModel()
                _isFavorite = isFavorite
                _state.postValue(UiState.Success(
                    header = ArtistHeaderItem(
                        title = artistPage.title,
                        subtitle = "",
                        thumbnail = artistPage.thumbnail,
                        isFavorite = isFavorite
                    ),
                    tracks = artistPage.tracks.map { it.toThumbnailSmallItem() },
                    albums = artistPage.albums.map { it.toThumbnailLargeItem() },
                    singles = artistPage.singles.map { it.toThumbnailLargeItem() },
                    relatedPlaylists = artistPage.relatedPlaylists.map { it.toThumbnailMediumItem() },
                    similar = artistPage.similar.map { it.toThumbnailRoundedItem() }
                ))
            }
        }
    }

    fun getShareLink(): String = artistModel?.shareLink ?: ""

    fun toggleFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            artistModel?.let { artist ->
                if(_isFavorite){
                    repository.unLikeArtist(artist)
                } else {
                    repository.likeArtist(artist)
                }
            }
            updateArtist()
        }
    }

    fun playAll() {
        (_state.value as? UiState.Success)?.tracks?.let { playerHandler.playNow(it.toMediaItems()) }
    }

    fun playShuffled() {
        (_state.value as? UiState.Success)?.tracks?.let { playerHandler.playShuffle(it.toMediaItems()) }
    }

    fun playTrack(id: String) {
        (_state.value as? UiState.Success)
            ?.tracks
            ?.firstOrNull { it.id == id }
            ?.let { playerHandler.playNow(it.toMediaItem()) }
    }
}
