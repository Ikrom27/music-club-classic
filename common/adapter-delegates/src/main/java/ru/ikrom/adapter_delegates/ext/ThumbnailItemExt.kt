package ru.ikrom.adapter_delegates.ext

import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import ru.ikrom.base_adapter.ThumbnailItem

@OptIn(UnstableApi::class)
fun ThumbnailItem.toMediaItem() = MediaItem.Builder()
    .setMediaId(id)
    .setUri(id)
    .setCustomCacheKey(id)
    .setTag(this)
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setTitle(title)
            .setArtist(subtitle)
            .setArtworkUri(Uri.parse(thumbnail))
            .build()
    )
    .build()

fun List<ThumbnailItem>.toMediaItems() = map { it.toMediaItem() }