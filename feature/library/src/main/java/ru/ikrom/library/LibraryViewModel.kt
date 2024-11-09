package ru.ikrom.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.ui.base_adapter.delegates.ThumbnailSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.getNames
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: IMediaRepository
): ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    fun update(){
        _uiState.value = UiState.onLoading
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                _uiState.postValue(UiState.onSuccessful(
                    repository.getLikedTracks().map {
                        ThumbnailSmallItem(
                            id = it.videoId,
                            title = it.title,
                            subtitle = it.album.artists.getNames(),
                            thumbnail = it.album.thumbnail
                        )
                    }
                ))
            }.onFailure {
                _uiState.postValue(UiState.onError)
            }
        }
    }
}
