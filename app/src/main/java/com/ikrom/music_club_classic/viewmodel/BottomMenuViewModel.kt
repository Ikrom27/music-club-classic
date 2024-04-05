package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikrom.music_club_classic.data.model.Track

class BottomMenuViewModel: ViewModel() {
    val trackLiveData = MutableLiveData<Track>()
}