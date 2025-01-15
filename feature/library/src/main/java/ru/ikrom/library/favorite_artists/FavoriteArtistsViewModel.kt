package ru.ikrom.library.favorite_artists

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.ikrom.fragment_list_editable.EditableStateViewModel
import ru.ikrom.adapter_delegates.delegates.ThumbnailRoundedSmallItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailRoundedSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class FavoriteArtistsViewModel @Inject constructor(
    private val repository: IMediaRepository
): EditableStateViewModel<ThumbnailRoundedSmallItem>() {

    init {
        observeLikedArtists()
    }

    private fun observeLikedArtists(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLikedArtists().collect { data ->
                refresh(data.map { it.toThumbnailRoundedSmallItem() })
            }
        }
    }

    override suspend fun getData(): List<ThumbnailRoundedSmallItem> {
        return repository.getLikedArtists().first().map {
            it.toThumbnailRoundedSmallItem()
        }
    }
}