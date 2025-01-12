package ru.ikrom.home

import ru.ikrom.ui.base_adapter.delegates.CardItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemMediumItem

sealed class UiState {
    data object Loading: UiState()
    data object Error: UiState()
    data class Success(
        val quickPickTracks: List<ThumbnailItemMediumItem>,
        val favoriteTracks: List<ThumbnailItemMediumItem>,
        val playlists: List<CardItem>
    ): UiState()
}