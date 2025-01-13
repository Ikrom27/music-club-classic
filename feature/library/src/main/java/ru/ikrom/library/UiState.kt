package ru.ikrom.library

import ru.ikrom.ui.base_adapter.delegates.ThumbnailItemMediumItem

sealed class UiState {
    data object Error: UiState()
    data object Loading: UiState()
    data class Success(
        val data: List<ThumbnailItemMediumItem>
    ): UiState()
}