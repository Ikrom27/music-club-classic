package ru.ikrom.player

import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import ru.ikrom.player.ext.getMediaItemQueue
import ru.ikrom.player.ext.hasOldTracks
import ru.ikrom.player.ext.isLastPlaying
import ru.ikrom.player.ext.toMediaItem
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

class PlayerHandlerImpl @Inject constructor(
    private val player: ExoPlayer,
): PlayerConnection(player), IPlayerHandler {
    val recommendedQueue: MutableList<MediaItem> = mutableListOf()
    val currentQueue = MutableLiveData<List<MediaItem>>()

    init {
        player.addListener(this)
        currentQueue.value = player.getMediaItemQueue()
    }

    override fun seekTo(position: Long){
        player.seekTo(position)
    }

    override fun seekToNext() {
        player.seekToNext()
    }

    override fun seekToPrevious() {
        player.seekToPrevious()
    }


    override fun playNow(tracks: List<TrackModel>){
        if (player.currentMediaItem != tracks.first().toMediaItem()){
            player.clearMediaItems()
            player.setMediaItems(tracks.map { it.toMediaItem() })
            player.prepare()
            player.playWhenReady = true
        }
    }

    override fun playNow(track: TrackModel){
        playNow(listOf(track))
    }

    override fun playNext(item: TrackModel){
        playNext(listOf(item))
    }

    override fun playNext(items: List<TrackModel>) {
        val tracks = items.map { it.toMediaItem() }
        val index = if (player.mediaItemCount == 0) 0 else player.currentMediaItemIndex + 1
        player.addMediaItems(index, tracks)
        player.prepare()
        player.playWhenReady = true
    }

    override fun addToQueue(item: TrackModel) {
        addToQueue(listOf(item))
    }

    override fun addToQueue(items: List<TrackModel>) {
        val tracks = items.map { it.toMediaItem() }
        player.addMediaItems(maxOf(0, player.mediaItemCount), tracks)
        player.prepare()
    }

    override fun toggleRepeat(){
        when(player.repeatMode) {
            Player.REPEAT_MODE_OFF -> player.repeatMode = Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> player.repeatMode = Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> player.repeatMode = Player.REPEAT_MODE_OFF
        }
    }

    override fun toggleShuffle(){
        player.shuffleModeEnabled = !player.shuffleModeEnabled
        currentQueue.value = player.getMediaItemQueue()
    }

    override fun togglePlayPause() {
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

    private fun addRecommendedTracks(){}

    private fun removeOldTracks(){
        player.removeMediaItems(0, player.currentMediaItemIndex - SAVE_LAST_TRACK_NUM)
    }

    companion object {
        private const val SAVE_LAST_TRACK_NUM = 20
    }
}