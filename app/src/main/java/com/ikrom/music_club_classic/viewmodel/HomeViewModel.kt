package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.media3.extractor.mp4.Track
import com.ikrom.music_club_classic.domain.RecommendedUseCase
import com.ikrom.music_club_classic.extensions.models.toThumbnailMediumItem
import com.ikrom.music_club_classic.ui.adapters.delegates.CardItem
import com.ikrom.music_club_classic.ui.adapters.delegates.ThumbnailMediumItem
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
    val userPlaylists = MutableLiveData<List<CardItem>>()
    val trackList = MutableLiveData<List<ThumbnailMediumItem>>()

    private val tracksModel = MutableLiveData<List<TrackModel>>()

    init {
        update()
    }

    fun getTrackById(id: String): TrackModel {
        return tracksModel.value!!.first { it.videoId == id }
    }

    fun update(){
        viewModelScope.launch {
            quickPick.postValue(recommendedUseCase.getRecommendedTracks())
        }
        viewModelScope.launch {
            userPlaylists.postValue(emptyList())
        }
        viewModelScope.launch {
            val tracks = repository.getTracksByQuery("Linkin park")
            tracksModel.postValue(tracks)
            trackList.postValue(tracks.map { it.toThumbnailMediumItem() })
        }
    }
}