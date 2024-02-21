package com.ikrom.music_club_classic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.ikrom.innertube.YouTube
import com.ikrom.innertube.models.AccountInfo
import com.ikrom.music_club_classic.data.data_source.AccountLocalDataStore
import com.ikrom.music_club_classic.data.data_source.AccountRemoteDataStore
import javax.inject.Inject

class AccountRepository @Inject constructor(
    val localDataStore: AccountLocalDataStore,
    val remoteDataStore: AccountRemoteDataStore,
) {
    init {
        YouTube.cookie = getCookie()
    }

    fun getCookie(): String{
        return localDataStore.getCookie()
    }

    fun getAccountInfo(): LiveData<AccountInfo>{
        return remoteDataStore.getAccountInfo()
    }

    fun saveCookie(cookie: String){
        Log.d(TAG, cookie)
        YouTube.cookie = cookie
        localDataStore.saveCookie(cookie)
    }

    companion object {
        private val TAG = "AccountRepository"
    }
}