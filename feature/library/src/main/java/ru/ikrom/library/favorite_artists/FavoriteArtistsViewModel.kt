package ru.ikrom.library.favorite_artists

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ikrom.fragment_list_editable.EditableStateViewModel
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedSmallItem
import ru.ikrom.ui.models.toThumbnailRoundedSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class FavoriteArtistsViewModel @Inject constructor(
    private val repository: IMediaRepository
): EditableStateViewModel<ThumbnailRoundedSmallItem>() {
    override suspend fun getData(): List<ThumbnailRoundedSmallItem> {
        return repository.getLikedArtists().map {
            it.toThumbnailRoundedSmallItem()
        }
    }
}