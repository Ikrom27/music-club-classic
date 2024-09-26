package com.ikrom.music_club_classic.ui.items

import com.ikrom.music_club_classic.ui.adapters.delegates.ArtistHeaderItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailLargeItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailRoundedItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailSmallItem

class ArtistPageItem(
    val header: ArtistHeaderItem?,
    val tracks: List<ThumbnailSmallItem>,
    val albums: List<ThumbnailLargeItem>,
    val singles: List<ThumbnailLargeItem>,
    val relatedPlaylists: List<ThumbnailMediumItem>,
    val similar: List<ThumbnailRoundedItem>
)