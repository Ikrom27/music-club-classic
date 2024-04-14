package com.ikrom.music_club_classic.viewmodel

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
    val serverStatus = MutableLiveData<Int>()
    private val contentList = ArrayList<Track>()
    private var lastQuery = ""

    fun updateSearchList(query: String = lastQuery) {
        if (query.isEmpty()){
            return
        }
        lastQuery = query
        updateLocalResult(query)
        viewModelScope.launch {
            updateGlobalResult(query)
        }
        viewModelScope.launch {
            updateServerStatus()
        }
    }

    fun updateLocalResult(query: String) {
        if (contentList.isNotEmpty()){
            localResultList.value = contentList.filter {
                it.title.lowercase().trim().contains(query.lowercase().trim())
            }
        }
    }

    suspend fun updateServerStatus() {
        repository.getServerStatus().asFlow().collect {status ->
            serverStatus.postValue(status)
        }
    }

    suspend fun updateGlobalResult(query: String){
        repository.getTracksByQuery(query).asFlow().collect {tracks ->
            if (tracks != null){
                globalResultList.value = tracks
            }
        }
    }

    fun addContentList(tracks: List<Track>){
        contentList.clear()
        contentList.addAll(tracks)
    }
}