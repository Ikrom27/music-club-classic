package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.repository.MusicServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MusicServiceRepository): ViewModel() {
    fun getTracks(query: String): LiveData<List<Track>> {
        return repository.getTracksByQuery(query)
    }

    fun getNewReleases(): LiveData<List<Album>>{
        return repository.getNewReleases()
    }
}