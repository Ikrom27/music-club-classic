package ru.ikrom.player_handler

import androidx.lifecycle.LiveData
import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow

interface IPlayerHandler {
    val currentMediaItemLiveData: LiveData<MediaItem?>
    val isPlayingLiveData: LiveData<Boolean>
    val totalDurationLiveData: LiveData<Long>
    val repeatModeLiveData: LiveData<Int>
    val shuffleModeLiveData: LiveData<Boolean>
    val currentPositionFlow: Flow<Long>

    fun seekTo(position: Long)
    fun playNow(items: List<MediaItem>)
    fun playShuffle(items: List<MediaItem>)
    fun playNow(items: MediaItem)
    fun playNext(item: MediaItem)
    fun playNext(items: List<MediaItem>)
    fun addToQueue(item: MediaItem)
    fun addToQueue(items: List<MediaItem>)
    fun toggleRepeat()
    fun toggleShuffle()
    fun togglePlayPause()
    fun seekToNext()
    fun seekToPrevious()
    fun setOnQueueChanged(onChanged: (List<MediaItem>) -> Unit)
    fun playInQueue(id: String)
    fun setOnTrackChanged(onChanged: (List<MediaItem>, pos: Int) -> Unit)
}