package ru.ikrom.artist


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toThumbnailLargeItem
import ru.ikrom.ui.models.toThumbnailMediumItem
import ru.ikrom.ui.models.toThumbnailRoundedItem
import ru.ikrom.ui.models.toThumbnailSmallItem
import ru.ikrom.ui.base_adapter.delegates.ArtistHeaderItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.ArtistPageModel
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
): DefaultStateViewModel<UiState>() {

    private val artistModelLiveData = MutableLiveData<ArtistPageModel>()
    private var _isFavorite = false
    private var lastId = ""

    fun updateArtist(artistId: String = lastId){
        lastId = artistId
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getArtistData(artistId) to
                repository.isFavoriteArtist(artistId)
            }.onSuccess { (artistPage, isFavorite) ->
                artistModelLiveData.postValue(artistPage)
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

    fun getShareLink(): String = artistModelLiveData.value?.shareLink ?: ""

    fun toggleFavorite(){
        viewModelScope.launch(Dispatchers.IO) {
            artistModelLiveData.value?.let { artistPage ->
                if(_isFavorite){
                    repository.unLikeArtist(artistPage.toArtistModel())
                } else {
                    repository.likeArtist(artistPage.toArtistModel())
                }
            }
            updateArtist()
        }
    }

    fun playAll(){
        artistModelLiveData.value?.tracks?.let { playerHandler.playNow(it) }
    }

    fun playShuffled(){
        artistModelLiveData.value?.tracks?.let { playerHandler.playNow(it.shuffled()) }
    }

    fun playTrackById(id: String){
        artistModelLiveData.value
            ?.tracks
            ?.first { it.videoId == id }
            ?.let { playerHandler.playNow(it) }
    }
}
