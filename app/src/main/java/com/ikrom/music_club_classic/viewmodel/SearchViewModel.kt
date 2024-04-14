package com.ikrom.music_club_classic.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val repository: MediaRepository
): ViewModel() {
    val globalResultList = MutableLiveData<List<Track>>()
    val localResultList = MutableLiveData<List<Track>>()
    private val contentList = ArrayList<Track>()
    private var lastQuery = ""

    fun updateSearchList(query: String = lastQuery) {
        lastQuery = query
        viewModelScope.launch {
            localResultList.value = contentList.filter {
                it.title.lowercase().trim().contains(query.lowercase().trim())
            }
            repository.getTracksByQuery(query).asFlow().collect {
                globalResultList.value = it
            }
            Log.d("SearchViewModel", localResultList.value?.size.toString())
        }
    }

    fun addContentList(tracks: List<Track>){
        contentList.clear()
        contentList.addAll(tracks)
    }
}