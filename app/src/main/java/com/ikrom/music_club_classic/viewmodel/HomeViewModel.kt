package com.ikrom.music_club_classic.viewmodel

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

    init {
        update()
    }

    fun getTracks(query: String): LiveData<List<Track>> {
        return repository.getTracksByQuery(query)
    }

    fun update(){
        viewModelScope.launch {
            recommendedUseCase.getRecommendedTracks().asFlow().collect {
                quickPick.postValue(it)
            }
            repository.getLikedPlayLists().asFlow().collect(){
                userPlaylists.postValue(it)
            }
        }
    }

    fun getRecommendedTracks(): LiveData<List<Track>>{
        return quickPick
    }

    fun getNewReleases(): LiveData<List<Album>>{
        return repository.getNewReleases()
    }

    fun getLikedPlayLists(): LiveData<List<PlayList>>{
        return userPlaylists
    }
}