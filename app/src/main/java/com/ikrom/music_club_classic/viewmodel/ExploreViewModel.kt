package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.AlbumModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    val repository: IMediaRepository
) : ViewModel() {
    val newReleasesList = MutableLiveData<List<AlbumModel>>()
    init {
        update()
    }
    fun update(){
        viewModelScope.launch {
            newReleasesList.postValue(repository.getNewReleases())
        }
    }
}