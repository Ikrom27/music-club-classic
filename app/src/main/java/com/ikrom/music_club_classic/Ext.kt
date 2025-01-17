package com.ikrom.music_club_classic

import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import ru.ikrom.youtube_data.model.TrackModel
import ru.ikrom.youtube_data.model.getNames


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
