package ru.ikrom.player_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.ui.base_adapter.delegates.PlayerQueueItem
import javax.inject.Inject

@HiltViewModel
class PlayerQueueViewModel @Inject constructor(
    playerHandler: IPlayerHandler
): ViewModel() {
    val currentMediaItem = playerHandler.currentMediaItemLiveData
    val currentQueue = MutableLiveData<List<PlayerQueueItem>>()
    var playingTrackId = ""

    init {
        playerHandler.setOnQueueChanged { mediaItems ->
                currentQueue.value = mediaItems.map {
                    playingTrackId = if (it.mediaId == currentMediaItem.value?.mediaId) it.mediaId else playingTrackId
                    PlayerQueueItem(
                        it.mediaId,
                        it.mediaMetadata.title.toString(),
                        it.mediaMetadata.artist.toString(),
                        it.mediaMetadata.artworkUri.toString(),
                    it.mediaId == currentMediaItem.value?.mediaId
                )
            }
        }
    }
}