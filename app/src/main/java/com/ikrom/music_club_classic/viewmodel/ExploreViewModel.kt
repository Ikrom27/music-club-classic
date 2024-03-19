package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    val repository: MediaRepository
) : ViewModel() {
    val newReleasesList = MutableLiveData<List<Album>>()
    init {
        update()
    }
    fun update(){
        viewModelScope.launch {
            repository.getNewReleases().asFlow().collect{
                newReleasesList.postValue(it)
            }
        }
    }
}