package com.ikrom.music_club_classic.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikrom.innertube.models.AccountInfo
import com.ikrom.music_club_classic.data.repository.AccountRepository
import com.ikrom.music_club_classic.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AccountRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val cookieLiveData = MutableLiveData("")
    val themeState = MutableLiveData(getThemeState())

    init {
        Log.d("AccountViewModel", "init")
    }

    fun getThemeState(): Int{
        return settingsRepository.getThemeMode()
    }

    fun setThemeState(themeMode: Int){
        themeState.value = themeMode
        settingsRepository.setThemeMode(themeMode)
    }

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