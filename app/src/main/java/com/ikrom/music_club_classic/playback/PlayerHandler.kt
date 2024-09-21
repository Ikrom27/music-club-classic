package com.ikrom.music_club_classic.playback

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import com.ikrom.music_club_classic.extensions.getMediaItemQueue
import com.ikrom.music_club_classic.extensions.hasOldTracks
import com.ikrom.music_club_classic.extensions.isLastPlaying
import com.ikrom.music_club_classic.extensions.toMediaItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

class PlayerHandler @Inject constructor(
    val player: ExoPlayer,
    val repository: IMediaRepository
): PlayerConnection(player) {
    val recommendedQueue: MutableList<MediaItem> = mutableListOf()
    val currentQueue = MutableLiveData<List<MediaItem>>()

    init {
        player.addListener(this)
        currentQueue.value = player.getMediaItemQueue()
    }

    fun playNow(tracks: List<TrackModel>){
        if (player.currentMediaItem != tracks.first().toMediaItem()){
            player.clearMediaItems()
            player.setMediaItems(tracks.map { it.toMediaItem() })
            player.prepare()
            player.playWhenReady = true
        }
    }

    fun playNow(track: TrackModel){
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
        if (player.mediaItemCount - player.currentMediaItemIndex - 1 == 0 || player.mediaItemCount == 0){
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
        currentQueue.value = player.getMediaItemQueue()
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

    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
        super.onTimelineChanged(timeline, reason)
        currentQueue.value = player.getMediaItemQueue()
    }

    private fun addRecommendedTracks(){
    }

    private fun removeOldTracks(){
        player.removeMediaItems(0, player.currentMediaItemIndex - SAVE_LAST_TRACK_NUM)
    }

    companion object {
        private const val SAVE_LAST_TRACK_NUM = 20
    }
}