package ru.ikrom.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ikrom.base_adapter.ThumbnailItem
import ru.ikrom.adapter_delegates.ext.toMediaItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailSmallItem
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository,
): DefaultStateViewModel<SearchUiState>() {
    private var searchJob: Job? = null

    private fun updateSearchList(query: String) {
        if (query.isBlank()) return
        _state.value = SearchUiState.Loading
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(DEBOUNCE_PERIOD)
            runCatching {
                repository.getTracksByQuery(query).map { it.toThumbnailSmallItem() }
            }.onSuccess { result ->
                _state.postValue(
                    if (result.isNotEmpty()) {
                        SearchUiState.Success(result)
                    } else {
                        SearchUiState.NoResult
                    }
                )
            }.onFailure {
                Log.d(TAG, it.message.toString())
                _state.postValue(SearchUiState.Error)
                return@launch
            }
        }
    }

    fun updateSearchRequest(request: String){
        updateSearchList(request)
    }

    fun playTrack(item: ThumbnailItem) {
        playerHandler.playNow(item.toMediaItem())
    }

    companion object {
        val TAG = SearchViewModel::class.simpleName
        const val DEBOUNCE_PERIOD: Long = 500
    }
}