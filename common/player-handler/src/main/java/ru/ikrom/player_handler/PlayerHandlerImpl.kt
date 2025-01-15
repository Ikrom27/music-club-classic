package ru.ikrom.player_handler

import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import ru.ikrom.youtube_data.model.TrackModel
import ru.ikrom.youtube_data.model.getNames
import javax.inject.Inject

@OptIn(UnstableApi::class)
internal fun TrackModel.toMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(this.videoId)
        .setUri(this.videoId)
        .setCustomCacheKey(this.videoId)
        .setTag(this)
        .setMediaMetadata(
            MediaMetadata.Builder()
                .setTitle(this.title)
                .setArtist(this.album.artists.getNames())
                .setArtworkUri(this.album.thumbnail.toUri())
                .build()
        )
        .build()
}


class PlayerHandlerImpl @Inject constructor(
    private val player: ExoPlayer,
): PlayerConnection(player), IPlayerHandler {
    var _onQueueChanged: (List<MediaItem>) -> Unit = {}

    init {
        player.addListener(this)
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
        updateQueue()
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
        updateQueue()
    }

    override fun playInQueue(id: String){
        player.seekTo(getMediaItemQueue().indexOfFirst { it.mediaId == id }, 0)
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
    }

    override fun togglePlayPause() {
        player.playWhenReady = !player.playWhenReady
    }

    override fun setOnQueueChanged(onChanged: (List<MediaItem>) -> Unit){
        _onQueueChanged = onChanged
        _onQueueChanged(getMediaItemQueue())
    }

    private fun updateQueue(){
        _onQueueChanged(getMediaItemQueue())
    }

    private fun getMediaItemQueue(): List<MediaItem> {
        val timeline = player.currentTimeline
        if (timeline.isEmpty) {
            return emptyList()
        }
        val queue = ArrayDeque<MediaItem>()
        val queueSize = timeline.windowCount

        val currentMediaItemIndex: Int = player.currentMediaItemIndex
        queue.add(timeline.getWindow(currentMediaItemIndex, Timeline.Window()).mediaItem)

        var firstMediaItemIndex = currentMediaItemIndex
        var lastMediaItemIndex = currentMediaItemIndex
        val shuffleModeEnabled = player.shuffleModeEnabled
        while ((firstMediaItemIndex != C.INDEX_UNSET || lastMediaItemIndex != C.INDEX_UNSET) && queue.size < queueSize) {
            if (lastMediaItemIndex != C.INDEX_UNSET) {
                lastMediaItemIndex = timeline.getNextWindowIndex(
                    lastMediaItemIndex,
                    Player.REPEAT_MODE_OFF, shuffleModeEnabled
                )
                if (lastMediaItemIndex != C.INDEX_UNSET) {
                    queue.add(timeline.getWindow(lastMediaItemIndex, Timeline.Window()).mediaItem)
                }
            }
            if (firstMediaItemIndex != C.INDEX_UNSET && queue.size < queueSize) {
                firstMediaItemIndex = timeline.getPreviousWindowIndex(
                    firstMediaItemIndex,
                    Player.REPEAT_MODE_OFF, shuffleModeEnabled
                )
                if (firstMediaItemIndex != C.INDEX_UNSET) {
                    queue.addFirst(
                        timeline.getWindow(
                            firstMediaItemIndex,
                            Timeline.Window()
                        ).mediaItem
                    )
                }
            }
        }
        return queue.toList()
    }
}