package ru.ikrom.album

import ru.ikrom.adapter_delegates.delegates.ThumbnailHeaderItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem

data class AlbumPageContent(
    val header: ThumbnailHeaderItem,
    val tracks: List<ThumbnailSmallItem>
)