package com.ikrom.music_club_classic.playback

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.toMediaItem
import javax.inject.Inject

class PlayerHandler @Inject constructor(
    val player: ExoPlayer
): PlayerConnection(player) {

    init {
        player.addListener(this)
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
}