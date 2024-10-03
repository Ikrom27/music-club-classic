package ru.ikrom.album

import ru.ikrom.ui.base_adapter.delegates.ThumbnailHeaderItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem

sealed class AlbumUiState {
    data object Loading: AlbumUiState()
    data object Error: AlbumUiState()
    data class Success(
        val header: ThumbnailHeaderItem,
        val tracks: List<ThumbnailSmallItem>
    ): AlbumUiState()
}