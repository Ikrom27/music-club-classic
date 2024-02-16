package com.ikrom.music_club_classic.playback

import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MusicServiceRepository
import com.ikrom.music_club_classic.extensions.toMediaItem
import javax.inject.Inject

open class PlayerConnection (
    player: ExoPlayer,
): Player.Listener {
    open var currentMediaItem = MutableLiveData(player.currentMediaItem)
    open val isPlaying = MutableLiveData(player.playWhenReady)
    open var totalDuration =  MutableLiveData(0L)

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        currentMediaItem.value = mediaItem
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        isPlaying.value = playWhenReady
    }

    override fun onEvents(player: Player, events: Player.Events) {
        if (player.duration > 0L){
            totalDuration.postValue(player.duration)
        }
    }
}