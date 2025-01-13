package ru.ikrom.artist

import ru.ikrom.ui.base_adapter.delegates.ArtistHeaderItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailLargeItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemMediumItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailRoundedItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem

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