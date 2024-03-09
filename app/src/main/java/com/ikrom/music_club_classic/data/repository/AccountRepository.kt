package com.ikrom.music_club_classic.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.ikrom.innertube.YouTube
import com.ikrom.innertube.models.AccountInfo
import com.ikrom.music_club_classic.data.data_source.account_data_source.AccountLocalDataSource
import com.ikrom.music_club_classic.data.data_source.account_data_source.AccountRemoteDataSource
import javax.inject.Inject

class AccountRepository @Inject constructor(
    val localDataStore: AccountLocalDataSource,
    val remoteDataStore: AccountRemoteDataSource,
) {
    init {
        YouTube.cookie = getCookie()
    }

    fun isAuthorised(): Boolean {
        return getCookie() != ""
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