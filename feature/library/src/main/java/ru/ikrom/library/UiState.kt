package ru.ikrom.library

import ru.ikrom.adapter_delegates.delegates.ThumbnailItemMediumItem

sealed class UiState {
    data object Error: UiState()
    data object Loading: UiState()
    data class Success(
        val data: List<ThumbnailItemMediumItem>
    ): UiState()
}