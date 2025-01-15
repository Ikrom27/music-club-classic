package ru.ikrom.album

import ru.ikrom.adapter_delegates.delegates.ThumbnailHeaderItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem

sealed class AlbumUiState {
    data object Loading: AlbumUiState()
    data object Error: AlbumUiState()
    data class Success(
        val header: ThumbnailHeaderItem,
        val tracks: List<ThumbnailSmallItem>
    ): AlbumUiState()
}