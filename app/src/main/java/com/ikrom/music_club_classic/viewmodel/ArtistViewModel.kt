package com.ikrom.music_club_classic.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.ArtistPageModel
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(
    val repository: IMediaRepository
): ViewModel() {
    val artistLiveData = MutableLiveData<ArtistPageModel>()
    var artistId = ""
        set(value) {
            field = value
            updateArtistData(value)
        }

    fun updateArtistData(artistId: String){
        viewModelScope.launch {
            artistLiveData.postValue(repository.getArtistData(artistId))
        }
    }
}
