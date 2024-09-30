package ru.ikrom.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toThumbnailSmallItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.player.PlayerHandlerImpl
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val playerHandler: PlayerHandlerImpl,
    private val repository: IMediaRepository,
    private val navigate: Navigate
): ViewModel() {
    val searchUiResult = MutableLiveData<List<ThumbnailSmallItem>>(emptyList())
    private var searchModelResult: List<TrackModel> = emptyList()

    private val _searchRequest = MutableLiveData("")

    private fun updateSearchList(query: String) {
        viewModelScope.launch {
            val tracks = repository.getTracksByQuery(query)
            searchModelResult = tracks
            searchUiResult.postValue(
                tracks.map { it.toThumbnailSmallItem() }
            )
        }
    }

    private fun getTrackById(id: String): TrackModel{
        return searchModelResult.first {
            it.videoId == id
        }
    }

    fun updateSearchRequest(request: String){
        _searchRequest.value = request
        updateSearchList(request)
    }

    fun playTrackById(id: String) {
        playerHandler.playNow(getTrackById(id))
    }

    fun toTrackMenu(id: String){
        navigate.toBottomMenu(getTrackById(id))
    }

    fun navigateUp(){
        navigate.navigateUp()
    }

    interface Navigate{
        fun toBottomMenu(track: TrackModel)
        fun navigateUp()
    }
}