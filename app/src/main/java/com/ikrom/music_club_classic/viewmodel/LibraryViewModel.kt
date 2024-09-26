package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikrom.music_club_classic.ui.adapters.LibraryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.PlaylistModel
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: IMediaRepository
): ViewModel() {
    val likedPlaylists = MutableLiveData<List<LibraryItem>>()

    fun updateLikedPlayLists() {
        likedPlaylists.postValue(emptyList())
    }
}