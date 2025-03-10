package ru.ikrom.playlist

import ru.ikrom.adapter_delegates.delegates.ThumbnailHeaderItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem

data class PlaylistPageContent (
    val header: ThumbnailHeaderItem,
    val tracks: List<ThumbnailSmallItem>
)