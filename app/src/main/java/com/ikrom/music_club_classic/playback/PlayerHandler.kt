package com.ikrom.music_club_classic.playback

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import com.ikrom.innertube.models.WatchEndpoint
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MediaRepository
import com.ikrom.music_club_classic.extensions.hasOldTracks
import com.ikrom.music_club_classic.extensions.isLastPlaying
import com.ikrom.music_club_classic.extensions.toMediaItem
import javax.inject.Inject

class PlayerHandler @Inject constructor(
    val player: ExoPlayer,
    val repository: MediaRepository
): PlayerConnection(player) {
    private val playerQueue: MutableList<MediaItem> = mutableListOf()

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
        if (player.mediaItemCount - player.currentMediaItemIndex - 1 == 0){
            player.addMediaItems(items)
        } else {
            player.addMediaItems(player.mediaItemCount-1, items)
        }
        player.prepare()
    }


    fun toggleRepeat(){
        when(player.repeatMode) {
            Player.REPEAT_MODE_OFF -> player.repeatMode = Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> player.repeatMode = Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> player.repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    fun toggleShuffle(){
        player.shuffleModeEnabled = !player.shuffleModeEnabled
    }

    fun togglePlayPause() {
        player.playWhenReady = !player.playWhenReady
    }

    override fun onTracksChanged(tracks: Tracks) {
        super.onTracksChanged(tracks)
        if(player.hasOldTracks(SAVE_LAST_TRACK_NUM)){
            removeOldTracks()
        }
        if (player.isLastPlaying()) {
            addRecommendedTracks()
        }
    }

    private fun addRecommendedTracks(){
        if (playerQueue.size < 2){
            repository.getRadioTracks(WatchEndpoint(player.currentMediaItem?.mediaId)).observeForever { trackList ->
                if (trackList.isNotEmpty()){
                    playerQueue += trackList.map{it.toMediaItem()}.shuffled()
                    if(player.hasNextMediaItem()){

                    }
                    addToQueue(playerQueue.removeFirst())
                }
            }
        } else {
            addToQueue(playerQueue.removeFirst())
        }
    }

    private fun removeOldTracks(){
        player.removeMediaItems(0, player.currentMediaItemIndex - SAVE_LAST_TRACK_NUM)
    }

    companion object {
        private const val SAVE_LAST_TRACK_NUM = 20
    }
}