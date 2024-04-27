package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.ikrom.music_club_classic.data.model.Track

class BottomMenuViewModel: ViewModel() {
    var navController: NavController? = null
    val trackLiveData = MutableLiveData<Track>()
}