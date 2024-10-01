package ru.ikrom.explore

import ru.ikrom.ui.base_adapter.delegates.CardItem

sealed class ExploreUiState(){
    data object Loading: ExploreUiState()
    data object Error: ExploreUiState()
    data class Success(val data: List<CardItem>): ExploreUiState()
}