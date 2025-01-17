package ru.ikrom.home

import ru.ikrom.adapter_delegates.delegates.CardItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemGrid
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemMediumItem

sealed class UiState {
    data object Loading: UiState()
    data class Success (
        val quickPickTracks: List<ThumbnailItemGrid>,
        val favoriteTracks: List<ThumbnailItemMediumItem>,
        val playlists: List<CardItem>
    ): UiState()
}