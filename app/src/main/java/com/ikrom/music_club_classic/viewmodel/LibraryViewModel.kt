package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ikrom.music_club_classic.data.model.Playlist
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: MediaRepository
): ViewModel() {

    fun getLikedPlayLists(): LiveData<List<Playlist>> {
        return repository.getLikedPlayLists()
    }
}