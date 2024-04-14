package com.ikrom.music_club_classic.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MediaRepository
import com.ikrom.music_club_classic.domain.RecommendedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val recommendedUseCase: RecommendedUseCase
): ViewModel() {
    val quickPick = MutableLiveData<List<Track>>()
    val userPlaylists = MutableLiveData<List<PlayList>>()
    val trackList = MutableLiveData<List<Track>>()

    init {
        update()
    }

    fun update(){
        viewModelScope.launch {
            recommendedUseCase.getRecommendedTracks().asFlow().collect {
                quickPick.postValue(it)
            }
        }
        viewModelScope.launch {
            repository.getLikedPlayLists().asFlow().collect(){
                userPlaylists.postValue(it)
            }
        }
        viewModelScope.launch {
            repository.getTracksByQuery("Linkin park").asFlow().collect(){
                if (it != null){
                    trackList.postValue(it)
                }
            }
        }
    }
}