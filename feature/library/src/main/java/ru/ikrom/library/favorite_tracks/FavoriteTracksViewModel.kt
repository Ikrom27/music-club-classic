package ru.ikrom.library.favorite_tracks

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import ru.ikrom.fragment_list_editable.EditableStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem
import ru.ikrom.ui.base_adapter.delegates.toMediaItem
import ru.ikrom.ui.models.toThumbnailSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class FavoriteTracksViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository
): EditableStateViewModel<ThumbnailSmallItem>() {
    private val tracks = mutableMapOf<String, TrackModel>()
    override suspend fun getData(): List<ThumbnailSmallItem> {
        tracks.clear()
        return repository.getLikedTracks().first().map {
            tracks[it.videoId] = it
            it.toThumbnailSmallItem()
        }
    }

    fun playTrack(item: ThumbnailItem){
        playerHandler.playNow(item.toMediaItem())
    }
}