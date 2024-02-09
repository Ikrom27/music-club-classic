package com.ikrom.music_club_classic.playback

import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.toMediaItem
import javax.inject.Inject

class PlayerConnection @Inject constructor(
    val player: ExoPlayer
): Player.Listener {
    private var currentMediaItem = MutableLiveData(player.currentMediaItem)
    val playbackState = MutableLiveData(player.playbackState)
    val isPlaying = MutableLiveData(player.playWhenReady)
    var currentPosition = MutableLiveData(0L)
    var totalDuration =  MutableLiveData(0L)

    init {
        player.addListener(this)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        currentMediaItem.value = mediaItem
    }

    override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
        isPlaying.value = playWhenReady
    }

    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        totalDuration.value = player.duration
        currentPosition.value = player.currentPosition
    }

    fun playNow(tracks: List<Track>){
        if (player.currentMediaItem != tracks.first().toMediaItem()){
            player.clearMediaItems()
            player.setMediaItems(tracks.map { it.toMediaItem() })
            player.prepare()
            player.playWhenReady = true
        }
    }

    fun playNow(track: Track){
        playNow(listOf(track))
    }

    fun playNext(item: MediaItem){
        playNext(listOf(item))
    }

    fun playNext(items: List<MediaItem>) {
        player.addMediaItems(if (player.mediaItemCount == 0) 0 else player.currentMediaItemIndex + 1, items)
        player.prepare()
        player.playWhenReady = true
    }

    fun addToQueue(item: MediaItem) {
        addToQueue(listOf(item))
    }

    fun addToQueue(items: List<MediaItem>) {
        player.addMediaItems(items)
        player.prepare()
    }

    fun getCurrentMediaItem(): MutableLiveData<MediaItem?> {
        return currentMediaItem
    }
}