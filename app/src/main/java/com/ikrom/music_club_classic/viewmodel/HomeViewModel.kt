package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.ikrom.ui.models.toThumbnailMediumItem
import ru.ikrom.ui.base_adapter.delegates.CardItem
import ru.ikrom.ui.base_adapter.delegates.ThumbnailMediumItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.player.IPlayerHandler
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val playerHandler: IPlayerHandler,
    private val repository: IMediaRepository,
): ViewModel() {
    val quickPick = MutableLiveData<List<TrackModel>>()
    val userPlaylists = MutableLiveData<List<CardItem>>()
    val trackList = MutableLiveData<List<ThumbnailMediumItem>>()
    val currentTrack = playerHandler.currentMediaItemLiveData
    val isPlaying = playerHandler.isPlayingLiveData

    private val tracksModel = MutableLiveData<List<TrackModel>>()

    init {
        update()
    }

    fun getTrackById(id: String): TrackModel {
        return tracksModel.value!!.first { it.videoId == id }
    }

    fun update(){
        viewModelScope.launch {
            userPlaylists.postValue(emptyList())
        }
        viewModelScope.launch {
            val tracks = repository.getTracksByQuery("Linkin park")
            tracksModel.postValue(tracks)
            trackList.postValue(tracks.map { it.toThumbnailMediumItem() })
        }
    }

    fun playTrackById(id: String){
        playerHandler.playNow(getTrackById(id))
    }

    fun togglePlayPause(){
        playerHandler.togglePlayPause()
    }
}