package ru.ikrom.library.favorite_playlists

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.delegates.ThumbnailMediumPlaylistItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailMediumPlaylistItem
import ru.ikrom.fragment_list_editable.EditableStateViewModel
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.PlaylistModel
import javax.inject.Inject

@HiltViewModel
class FavoritePlaylistsViewModel @Inject constructor(
    private val repository: IMediaRepository
): EditableStateViewModel<ThumbnailMediumPlaylistItem>() {

    init {
        observeLikedArtists()
    }

    fun createPlaylist(title: String, thumbnailUrl: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.savePlaylist(PlaylistModel(title, title, null, thumbnailUrl))
        }
    }

    private fun observeLikedArtists(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLikedAlbums().collect { data ->
                updateState(data.map { it.toThumbnailMediumPlaylistItem() })
            }
        }
    }

    override suspend fun getData(): List<ThumbnailMediumPlaylistItem> {
        return repository.getSavedPlaylists().first().map {
            it.toThumbnailMediumPlaylistItem()
        }
    }

    override fun filterBy(item: ThumbnailMediumPlaylistItem, textQuery: String): Boolean {
        return item.filterWithText(textQuery)
    }
}