package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ru.ikrom.youtube_data.model.TrackModel

class BottomMenuViewModel: ViewModel() {
    var navController: NavController? = null
    val trackLiveData = MutableLiveData<TrackModel>()
}