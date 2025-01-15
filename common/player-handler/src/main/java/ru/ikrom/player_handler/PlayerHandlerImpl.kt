package ru.ikrom.player_handler

import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import javax.inject.Inject


class PlayerHandlerImpl @Inject constructor(
    private val player: ExoPlayer,
): PlayerConnection(player), IPlayerHandler {
    private var _onQueueChanged: (List<MediaItem>) -> Unit = {}

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


    override fun playNow(items: List<MediaItem>){
        if (player.currentMediaItem != items.first()){
            player.clearMediaItems()
            player.setMediaItems(items)
            player.prepare()
            player.playWhenReady = true
        }
        updateQueue()
    }

    override fun playNow(items: MediaItem){
        playNow(listOf(items))
    }

    override fun playShuffle(items: List<MediaItem>) {
        playNow(items.shuffled())
    }

    override fun playNext(item: MediaItem){
        playNext(listOf(item))
    }

    override fun playNext(items: List<MediaItem>) {
        val index = if (player.mediaItemCount == 0) 0 else player.currentMediaItemIndex + 1
        player.addMediaItems(index, items)
        player.prepare()
        player.playWhenReady = true
        updateQueue()
    }

    override fun playInQueue(id: String){
        player.seekTo(getMediaItemQueue().indexOfFirst { it.mediaId == id }, 0)
    }

    override fun addToQueue(item: MediaItem) {
        addToQueue(listOf(item))
    }

    override fun addToQueue(items: List<MediaItem>) {
        player.addMediaItems(maxOf(0, player.mediaItemCount), items)
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