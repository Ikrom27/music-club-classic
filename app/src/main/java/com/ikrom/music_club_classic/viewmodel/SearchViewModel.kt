package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val repository: MediaRepository
): ViewModel() {
    val searchList = MutableLiveData<List<Track>>()

    fun updateSearchList(query: String){
        viewModelScope.launch {
            repository.getTracksByQuery(query).asFlow().collect{
                searchList.value = it
            }
        }
    }
}