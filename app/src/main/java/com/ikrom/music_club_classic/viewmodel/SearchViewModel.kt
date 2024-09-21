package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    val repository: IMediaRepository
): ViewModel() {
    val globalResultList = MutableLiveData<List<TrackModel>>()
    val localResultList = MutableLiveData<List<TrackModel>>()
    val serverStatus = MutableLiveData<Int>()
    private val contentList = ArrayList<TrackModel>()
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
        serverStatus.postValue(200)
    }

    suspend fun updateGlobalResult(query: String){
        viewModelScope.launch {
            globalResultList.postValue(repository.getTracksByQuery(query))
        }
    }

    fun addContentList(tracks: List<TrackModel>){
        contentList.clear()
        contentList.addAll(tracks)
    }
}