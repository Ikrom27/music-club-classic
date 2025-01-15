package ru.ikrom.player_handler

import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class PlayerConnection (
    player: ExoPlayer,
): Player.Listener {
    val currentMediaItemLiveData = MutableLiveData(player.currentMediaItem)
    val isPlayingLiveData = MutableLiveData(player.playWhenReady)
    val totalDurationLiveData =  MutableLiveData(0L)
    val repeatModeLiveData = MutableLiveData(player.repeatMode)
    val shuffleModeLiveData = MutableLiveData(player.shuffleModeEnabled)
    val currentPositionFlow: Flow<Long> = flow {
        while (true) {
            emit(player.currentPosition)
            delay(100)
        }
    }.flowOn(Dispatchers.Main)


    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        currentMediaItemLiveData.value = mediaItem
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        isPlayingLiveData.value = playWhenReady
    }

    override fun onRepeatModeChanged(repeatMode: Int) {
        repeatModeLiveData.value = repeatMode
    }

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
        shuffleModeLiveData.value = shuffleModeEnabled
    }

    override fun onEvents(player: Player, events: Player.Events) {
        if (player.duration > 0L){
            totalDurationLiveData.postValue(player.duration)
        }
    }
}