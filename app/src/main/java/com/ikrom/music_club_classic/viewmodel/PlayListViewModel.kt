package com.ikrom.music_club_classic.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(
    val repository: MediaRepository
) : ViewModel() {
    val currentPlaylist = MutableLiveData<PlayList>()


    fun setPlaylist(playlist: PlayList){
        currentPlaylist.postValue(playlist)
    }

    fun getPlaylistItems(): LiveData<List<Track>>{
        Log.d("PlayListViewModel", currentPlaylist.value?.id ?: "")
        return repository.getPlaylistTracks(currentPlaylist.value?.id ?: "")
    }
}