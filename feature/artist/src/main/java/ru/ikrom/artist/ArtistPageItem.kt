package ru.ikrom.artist

import ru.ikrom.ui.base_adapter.delegates.ArtistHeaderItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailLargeItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailMediumItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem

class ArtistPageItem(
    val header: ArtistHeaderItem?,
    val tracks: List<ThumbnailSmallItem>,
    val albums: List<ThumbnailLargeItem>,
    val singles: List<ThumbnailLargeItem>,
    val relatedPlaylists: List<ThumbnailMediumItem>,
    val similar: List<ThumbnailRoundedItem>
)