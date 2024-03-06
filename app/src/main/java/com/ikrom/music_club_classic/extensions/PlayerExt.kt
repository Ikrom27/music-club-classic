package com.ikrom.music_club_classic.extensions

import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.playback.PlayerHandler


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
                .setArtworkUri(this.album.cover.toUri())
                .build()
        )
        .build()
}

fun Player.findNextMediaItemById(mediaId: String): MediaItem? {
    for (i in currentMediaItemIndex until mediaItemCount) {
        if (getMediaItemAt(i).mediaId == mediaId) {
            return getMediaItemAt(i)
        }
    }
    return null
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
