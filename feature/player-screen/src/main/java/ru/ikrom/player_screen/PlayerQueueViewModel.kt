package ru.ikrom.player_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.ui.base_adapter.delegates.PlayerQueueItem
import javax.inject.Inject

@HiltViewModel
class PlayerQueueViewModel @Inject constructor(
    val playerHandler: IPlayerHandler
): ViewModel() {
    val currentMediaItem = playerHandler.currentMediaItemLiveData
    val currentQueue = MutableLiveData<List<PlayerQueueItem>>()
    var playingTrackPosition = 0
    var prevPlayerTrackPosition = 0

    init {
        playerHandler.setOnQueueChanged { mediaItems ->
                currentQueue.value = mediaItems.mapIndexed { index, mediaItem ->
                    if(mediaItem.mediaId == currentMediaItem.value?.mediaId){
                        playingTrackPosition = index
                    }
                    PlayerQueueItem(
                        mediaItem.mediaId,
                        mediaItem.mediaMetadata.title.toString(),
                        mediaItem.mediaMetadata.artist.toString(),
                        mediaItem.mediaMetadata.artworkUri.toString(),
                        mediaItem.mediaId == currentMediaItem.value?.mediaId
                )
            }
        }
    }

    fun updatePositions(){
        prevPlayerTrackPosition = playingTrackPosition
        playingTrackPosition =
            (currentQueue.value?.indexOfFirst { it.id == currentMediaItem.value?.mediaId } ?: 0) + 1
    }

    fun playTrackById(id: String) {
        playerHandler.playInQueue(id)
    }
}