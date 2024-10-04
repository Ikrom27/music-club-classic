package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ikrom.youtube_data.IMediaRepository
import ru.ikrom.youtube_data.model.TrackModel
import javax.inject.Inject

@HiltViewModel
class BottomMenuViewModel @Inject constructor(
    private val repository: IMediaRepository
): ViewModel() {
    val trackLiveData = MutableLiveData<TrackModel>()

    fun updateTrackModel(id: String) {
        viewModelScope.launch {
            trackLiveData.postValue(
                repository.getTracksByQuery(id).first()
            )
        }
    }
}