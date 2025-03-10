package ru.ikrom.playlist.tracks_choise

import android.view.Display
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.ikrom.adapter_delegates.delegates.ThumbnailSmallItem
import ru.ikrom.adapter_delegates.modelsExt.toThumbnailSmallItem
import ru.ikrom.youtube_data.IMediaRepository
import javax.inject.Inject

@HiltViewModel
class ChoiceTracksViewModel @Inject constructor(
    private val repository: IMediaRepository
): ViewModel() {
    private val _tracksToChoice = MutableLiveData<List<ThumbnailSmallItem>>()
    val tracksToChoice: LiveData<List<ThumbnailSmallItem>> = _tracksToChoice

    private var playlistId: String? = null

    fun fetchData(id: String){
        playlistId = id
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPlaylistTracks(id)
                .combine(repository.getLikedTracks()) { playlistTracks, likedTracks ->
                    val playlistTrackIds = playlistTracks.map { it.videoId }.toSet()
                    likedTracks.filterNot { it.videoId in playlistTrackIds }
                }
                .collect { filteredTracks ->
                    _tracksToChoice.postValue(
                        filteredTracks.map { it.toThumbnailSmallItem() }
                    )
                }
        }
    }

    fun addToPlaylist(trackId: String){
        playlistId?.let {
            viewModelScope.launch(Dispatchers.IO) {
                repository.addTrackToPlaylist(playlistId!!, trackId)
            }
        }
    }
}