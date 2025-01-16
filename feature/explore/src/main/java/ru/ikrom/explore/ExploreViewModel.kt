package ru.ikrom.explore

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.adapter_delegates.modelsExt.toCardItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ikrom.base_fragment.DefaultStateViewModel
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.AlbumModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repository: IMediaRepository,
) : DefaultStateViewModel<ExplorePageContent>() {

    init {
        refresh()
    }

    private fun refresh(){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                repository.getNewReleases()
            }.onSuccess { albums ->
                _state.postValue(
                    ExplorePageContent(albums.map { it.toCardItem() })
                )
            }.onFailure {e ->
                Log.d(TAG, e.message.toString())
            }
        }
    }

    companion object {
        val TAG = ExploreViewModel::class.simpleName
    }
}