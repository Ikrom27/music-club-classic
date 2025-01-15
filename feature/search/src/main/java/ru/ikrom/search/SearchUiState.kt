package ru.ikrom.search

import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem

sealed class SearchUiState {
    data object Loading: SearchUiState()
    data object Error: SearchUiState()
    data object NoResult: SearchUiState()
    class Success(
        val data: List<ThumbnailSmallItem>
    ): SearchUiState()
}