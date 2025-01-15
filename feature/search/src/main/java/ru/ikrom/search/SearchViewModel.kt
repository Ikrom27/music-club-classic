package ru.ikrom.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.ui.base_adapter.delegates.ThumbnailItem
import ru.ikrom.ui.base_adapter.delegates.toMediaItem
import ru.ikrom.ui.models.toThumbnailSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository,
): ViewModel() {

    private val _uiState = MutableLiveData<SearchUiState>()
    val uiState: LiveData<SearchUiState> = _uiState

    private fun updateSearchList(query: String) {
        if (query.isBlank()) return
        _uiState.value = SearchUiState.Loading
        viewModelScope.launch {
            runCatching {
                repository.getTracksByQuery(query)
            }.onSuccess { result ->
                _uiState.postValue(
                    if (result.isNotEmpty()) {
                        SearchUiState.Success(result.map { it.toThumbnailSmallItem() })
                    } else {
                        SearchUiState.NoResult
                    }
                )
            }.onFailure {
                Log.d(TAG, it.message.toString())
                _uiState.postValue(SearchUiState.Error)
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
    }
}