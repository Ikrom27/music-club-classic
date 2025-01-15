package ru.ikrom.artist


import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.base.toMediaItem
import ru.ikrom.adapter_delegates.base.toMediaItems
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.adapter_delegates.delegates.ArtistHeaderItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailSmallItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailLargeItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailMediumItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailRoundedItem
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
