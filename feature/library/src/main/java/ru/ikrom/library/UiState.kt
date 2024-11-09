package ru.ikrom.library

import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem

sealed class UiState {
    data object onError: UiState()
    data object onLoading: UiState()
    data class onSuccessful(
        val data: List<ThumbnailSmallItem>
    ): UiState()
}