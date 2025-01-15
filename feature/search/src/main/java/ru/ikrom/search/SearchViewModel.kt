package ru.ikrom.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toThumbnailSmallItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.player_handler.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository,
): ViewModel() {

    private val _uiState = MutableLiveData<SearchUiState>()
    val uiState: LiveData<SearchUiState> = _uiState

    private var searchModelResult: List<TrackModel> = emptyList()

    private fun updateSearchList(query: String) {
        if (query.isBlank()) return
        _uiState.value = SearchUiState.Loading
        viewModelScope.launch {
            runCatching {
                repository.getTracksByQuery(query)
            }.getOrElse {
                Log.d(TAG, it.message.toString())
                _uiState.postValue(SearchUiState.Error)
                return@launch
            }.apply {
                searchModelResult = this
                _uiState.postValue(
                    if (isNotEmpty()) {
                        SearchUiState.Success(map { it.toThumbnailSmallItem() })
                    } else {
                        SearchUiState.NoResult
                    }
                )
            }
        }
    }


    private fun getTrackById(id: String): TrackModel{
        return searchModelResult.first {
            it.videoId == id
        }
    }

    fun updateSearchRequest(request: String){
        updateSearchList(request)
    }

    fun playTrackById(id: String) {
        playerHandler.playNow(getTrackById(id))
    }

    fun toTrackMenu(id: String){
//        navigate.toBottomMenu(getTrackById(id))
    }

    fun navigateUp(){
//        navigate.navigateUp()
    }

    companion object {
        val TAG = SearchViewModel::class.simpleName
    }
}