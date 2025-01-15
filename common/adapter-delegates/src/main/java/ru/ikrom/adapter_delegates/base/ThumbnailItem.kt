package ru.ikrom.adapter_delegates.base

import android.net.Uri
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.core.os.bundleOf
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi

interface ThumbnailItem {
    val id: String
    val title: String
    val subtitle: String
    val thumbnail: String
}

private const val ID = "id"
private const val TITLE = "title"
private const val SUBTITLE = "subtitle"
private const val THUMBNAIL = "thumbnail"

fun ThumbnailItem.toBundle(): Bundle {
    return bundleOf(
        ID to id,
        TITLE to title,
        SUBTITLE to subtitle,
        THUMBNAIL to thumbnail
    )
}

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

class ThumbnailArgs(bundle: Bundle) {
    val id: String = bundle.getString(ID) ?: ""
    val title: String = bundle.getString(TITLE) ?: ""
    val subtitle: String = bundle.getString(SUBTITLE) ?: ""
    val thumbnail: String = bundle.getString(THUMBNAIL) ?: ""
}