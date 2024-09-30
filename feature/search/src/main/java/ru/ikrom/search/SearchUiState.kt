package ru.ikrom.search

import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem

sealed class SearchUiState {
    data object Loading: SearchUiState()
    data object Error: SearchUiState()
    data object NoResult: SearchUiState()
    class Success(
        val data: List<ThumbnailSmallItem>
    ): SearchUiState()
}