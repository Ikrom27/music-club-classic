package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    val repository: MediaRepository
): ViewModel() {
    val searchList = MutableLiveData<List<Track>>()

    fun updateSearchList(query: String){
        repository.getTracksByQuery(query).observeForever {
            searchList.value = it
        }
    }
}