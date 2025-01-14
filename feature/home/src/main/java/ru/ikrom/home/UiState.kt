package ru.ikrom.home

import ru.ikrom.ui.base_adapter.delegates.CardItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemGrid
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemMediumItem

data class UiState (
    val quickPickTracks: List<ThumbnailItemGrid>,
    val favoriteTracks: List<ThumbnailItemMediumItem>,
    val playlists: List<CardItem>
)