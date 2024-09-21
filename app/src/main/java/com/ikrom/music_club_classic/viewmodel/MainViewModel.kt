package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    var bottomSheetState = BottomSheetBehavior.STATE_COLLAPSED
}