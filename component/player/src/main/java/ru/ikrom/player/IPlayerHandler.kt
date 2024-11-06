package ru.ikrom.player

import androidx.lifecycle.LiveData
import androidx.media3.common.MediaItem
import kotlinx.coroutines.flow.Flow
import ru.ikrom.youtube_data.model.TrackModel

interface IPlayerHandler {
    val currentMediaItemLiveData: LiveData<MediaItem?>
    val isPlayingLiveData: LiveData<Boolean>
    val totalDurationLiveData: LiveData<Long>
    val repeatModeLiveData: LiveData<Int>
    val shuffleModeLiveData: LiveData<Boolean>
    val currentPositionFlow: Flow<Long>

    fun seekTo(position: Long)
    fun playNow(tracks: List<TrackModel>)
    fun playNow(track: TrackModel)
    fun playNext(item: TrackModel)
    fun playNext(items: List<TrackModel>)
    fun addToQueue(item: TrackModel)
    fun addToQueue(items: List<TrackModel>)
    fun toggleRepeat()
    fun toggleShuffle()
    fun togglePlayPause()
    fun seekToNext()
    fun seekToPrevious()
}