package ru.ikrom.base_adapter

import android.os.Bundle
import androidx.core.os.bundleOf

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

class ThumbnailArgs(bundle: Bundle) {
    val id: String = bundle.getString(ID) ?: ""
    val title: String = bundle.getString(TITLE) ?: ""
    val subtitle: String = bundle.getString(SUBTITLE) ?: ""
    val thumbnail: String = bundle.getString(THUMBNAIL) ?: ""
}