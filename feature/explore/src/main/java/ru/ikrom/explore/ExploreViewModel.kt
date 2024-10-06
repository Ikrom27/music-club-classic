package ru.ikrom.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toCardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.AlbumModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: IMediaRepository,
) : ViewModel() {
    private var albumModels: List<AlbumModel> = ArrayList()
    private val _uiState = MutableLiveData<ExploreUiState>()
    val uiState: LiveData<ExploreUiState> = _uiState

    init {
        update()
    }

    private fun update(){
        _uiState.postValue(ExploreUiState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getNewReleases()
            }.onSuccess { albums ->
                albumModels = albums
                _uiState.postValue(
                    ExploreUiState.Success(albums.map { it.toCardItem() })
                )
            }.onFailure {e ->
                _uiState.postValue(ExploreUiState.Error)
                Log.d(TAG, e.message.toString())
            }
        }
    }

    companion object {
        val TAG = ExploreViewModel::class.simpleName
    }
}