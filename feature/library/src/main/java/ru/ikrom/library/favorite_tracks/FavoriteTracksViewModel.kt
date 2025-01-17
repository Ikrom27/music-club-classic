package ru.ikrom.library.favorite_tracks

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem
import ru.ikrom.adapter_delegates.ext.toMediaItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailSmallItem
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.fragment_list_editable.EditableStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class FavoriteTracksViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
): EditableStateViewModel<ThumbnailSmallItem>() {

    override suspend fun getData(): List<ThumbnailSmallItem> {
        return repository.getLikedTracks().first().map {
            it.toThumbnailSmallItem()
        }
    }

    fun playTrack(item: ThumbnailItem){
        playerHandler.playNow(item.toMediaItem())
    }

    override fun filterBy(item: ThumbnailSmallItem, textQuery: String): Boolean {
        return item.filterWithText(textQuery)
    }
}