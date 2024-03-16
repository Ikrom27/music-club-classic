package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.ViewModel
import com.ikrom.music_club_classic.data.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebAuthorizationViewModel @Inject constructor(
    val repository: AccountRepository
) : ViewModel() {
    fun saveCookie(cookie: String){
        repository.saveCookie(cookie)
    }
}