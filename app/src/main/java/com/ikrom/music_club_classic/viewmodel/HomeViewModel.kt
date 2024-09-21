package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.domain.RecommendedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.PlaylistModel
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IMediaRepository,
    private val recommendedUseCase: RecommendedUseCase
): ViewModel() {
    val quickPick = MutableLiveData<List<TrackModel>>()
    val userPlaylists = MutableLiveData<List<PlaylistModel>>()
    val trackList = MutableLiveData<List<TrackModel>>()

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
            userPlaylists.postValue(emptyList())
        }
        viewModelScope.launch {
            trackList.postValue(repository.getTracksByQuery("Linkin park"))
        }
    }
}