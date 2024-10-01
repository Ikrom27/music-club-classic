package ru.ikrom.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toCardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.AlbumModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    val repository: IMediaRepository,
) : ViewModel() {
    private val _uiState = MutableLiveData<ExploreUiState>()
    val uiState: LiveData<ExploreUiState> = _uiState

    private var albumModels: List<AlbumModel> = ArrayList()

    init {
        update()
    }

    fun update(){
        _uiState.postValue(ExploreUiState.Loading)
        viewModelScope.launch {
            runCatching {
                repository.getNewReleases()
            }.getOrElse {e ->
                _uiState.postValue(ExploreUiState.Error)
                Log.d(TAG, e.message.toString())
                return@launch
            }.apply {
                albumModels = this
                _uiState.postValue(
                    ExploreUiState.Success(map { it.toCardItem() })
                )
            }
        }
    }

    private fun getAlbumById (id: String): AlbumModel{
        return albumModels.first{ it.id == id }
    }

    companion object {
        val TAG = ExploreViewModel::class.simpleName
    }
}