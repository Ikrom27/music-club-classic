package ru.ikrom.home

import ru.ikrom.adapter_delegates.delegates.CardItem
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemGrid
import ru.ikrom.adapter_delegates.delegates.ThumbnailItemMediumItem

data class UiState (
    val quickPickTracks: List<ThumbnailItemGrid>,
    val favoriteTracks: List<ThumbnailItemMediumItem>,
    val playlists: List<CardItem>
)