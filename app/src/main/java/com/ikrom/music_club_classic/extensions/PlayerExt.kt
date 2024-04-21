package com.ikrom.music_club_classic.extensions

import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.models.getNames
import java.util.ArrayDeque


val Player.currentMetaData: MediaMetadata?
    get() = currentMediaItem?.mediaMetadata

fun Player.isLastPlaying(): Boolean{
    return currentMediaItemIndex + 1 >= mediaItemCount
}

fun Player.hasOldTracks(n: Int): Boolean{
    return currentMediaItemIndex > n
}

@OptIn(UnstableApi::class)
fun Track.toMediaItem(): MediaItem {
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

fun Player.getMediaItemQueue(): List<MediaItem> {
    val timeline = currentTimeline
    if (timeline.isEmpty) {
        return emptyList()
    }
    val queue = ArrayDeque<MediaItem>()
    val queueSize = timeline.windowCount

    val currentMediaItemIndex: Int = currentMediaItemIndex
    queue.add(timeline.getWindow(currentMediaItemIndex, Timeline.Window()).mediaItem)

    var firstMediaItemIndex = currentMediaItemIndex
    var lastMediaItemIndex = currentMediaItemIndex
    val shuffleModeEnabled = shuffleModeEnabled
    while ((firstMediaItemIndex != C.INDEX_UNSET || lastMediaItemIndex != C.INDEX_UNSET) && queue.size < queueSize) {
        if (lastMediaItemIndex != C.INDEX_UNSET) {
            lastMediaItemIndex = timeline.getNextWindowIndex(lastMediaItemIndex,
                Player.REPEAT_MODE_OFF, shuffleModeEnabled)
            if (lastMediaItemIndex != C.INDEX_UNSET) {
                queue.add(timeline.getWindow(lastMediaItemIndex, Timeline.Window()).mediaItem)
            }
        }
        if (firstMediaItemIndex != C.INDEX_UNSET && queue.size < queueSize) {
            firstMediaItemIndex = timeline.getPreviousWindowIndex(firstMediaItemIndex,
                Player.REPEAT_MODE_OFF, shuffleModeEnabled)
            if (firstMediaItemIndex != C.INDEX_UNSET) {
                queue.addFirst(timeline.getWindow(firstMediaItemIndex, Timeline.Window()).mediaItem)
            }
        }
    }
    return queue.toList()
}

fun Long.toTimeString(): String {
    val hours = this / 1000 / 3600
    val minutes = this / 1000 % 3600 / 60
    val seconds = this / 1000 % 60
    return when {
        hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else -> String.format("%02d:%02d", minutes, seconds)
    }
}
