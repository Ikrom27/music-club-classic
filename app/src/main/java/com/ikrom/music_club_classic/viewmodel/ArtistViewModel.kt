package com.ikrom.music_club_classic.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.ikrom.music_club_classic.data.model.ArtistData
import com.ikrom.music_club_classic.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    val repository: MediaRepository
): ViewModel() {
    val artistLiveData = MutableLiveData<ArtistData>()
    var artistId = ""
        set(value) {
            field = value
            updateArtistData(value)
        }

    fun updateArtistData(artistId: String){
        viewModelScope.launch {
            repository.getArtistData(artistId).asFlow().collect {
                artistLiveData.postValue(it)
            }
        }
    }
}
