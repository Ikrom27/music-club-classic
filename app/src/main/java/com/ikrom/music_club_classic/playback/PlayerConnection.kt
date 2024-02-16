package com.ikrom.music_club_classic.playback

import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

open class PlayerConnection (
    player: ExoPlayer,
): Player.Listener {
    open val currentMediaItemLiveData = MutableLiveData(player.currentMediaItem)
    open val isPlayingLiveData = MutableLiveData(player.playWhenReady)
    open val totalDurationLiveData =  MutableLiveData(0L)
    open val repeatModeLiveData = MutableLiveData(player.repeatMode)
    open val shuffleModeLiveData = MutableLiveData(player.shuffleModeEnabled)

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