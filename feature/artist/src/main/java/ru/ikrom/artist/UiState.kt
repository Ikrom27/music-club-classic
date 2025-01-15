package ru.ikrom.artist

import ru.ikrom.adapter_delegates.delegates.ArtistHeaderItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailLargeItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemMediumItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailRoundedItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem

sealed class UiState {
    data object Loading: UiState()
    data class Error(val e: Throwable): UiState()
    data class Success(
        val header: ArtistHeaderItem?,
        val tracks: List<ThumbnailSmallItem>,
        val albums: List<ThumbnailLargeItem>,
        val singles: List<ThumbnailLargeItem>,
        val relatedPlaylists: List<ThumbnailItemMediumItem>,
        val similar: List<ThumbnailRoundedItem>
    ): UiState()
}