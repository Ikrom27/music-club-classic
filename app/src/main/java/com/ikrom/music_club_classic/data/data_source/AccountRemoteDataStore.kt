package com.ikrom.music_club_classic.data.data_source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ikrom.innertube.YouTube
import com.ikrom.innertube.models.AccountInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountRemoteDataStore {
    fun getAccountInfo(): LiveData<AccountInfo> {
        val accountInfo = MutableLiveData<AccountInfo>()
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.accountInfo().onSuccess { result ->
                accountInfo.postValue(result)
                Log.d(TAG, result.email)
            }.onFailure {
                Log.e(TAG, "onFailure error: $it")
            }
        }
        return accountInfo
    }

    companion object {
        private val TAG = this::class.java.name
    }
}