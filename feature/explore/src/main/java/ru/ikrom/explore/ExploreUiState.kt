package ru.ikrom.explore

import ru.ikrom.adapter_delegates.delegates.CardItem

sealed class ExploreUiState(){
    data object Loading: ExploreUiState()
    data object Error: ExploreUiState()
    data class Success(val data: List<CardItem>): ExploreUiState()
}