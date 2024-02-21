package com.ikrom.music_club_classic.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikrom.innertube.models.AccountInfo
import com.ikrom.music_club_classic.data.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository
): ViewModel() {
    private val cookieLiveData = MutableLiveData("")

    fun saveCookie(cookie: String){
        cookieLiveData.value = cookie
        repository.saveCookie(cookie)
    }

    fun getAccountInfo(): LiveData<AccountInfo> {
        return repository.getAccountInfo()
    }

    fun clearCookie(){
        saveCookie("")
    }

    fun getCookie(): String {
//        cookieLiveData.value = repository.getCookie()
        return repository.getCookie()
    }
}