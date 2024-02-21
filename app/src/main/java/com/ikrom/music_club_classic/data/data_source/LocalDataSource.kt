package com.ikrom.music_club_classic.data.data_source

import androidx.lifecycle.MutableLiveData
import com.ikrom.music_club_classic.data.model.SearchHistory
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.data.model.TrackEntity
import com.ikrom.music_club_classic.data.room.AppDao
import com.ikrom.music_club_classic.data.room.AppDataBase
import com.ikrom.music_club_classic.extensions.toTrack
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class LocalDataSource @Inject constructor(dataBase: AppDataBase) {
    private val appDao: AppDao = dataBase.dao()
    suspend fun insertTrack(track: TrackEntity) {
        appDao.insertTrack(track)
    }

    suspend fun isFavorite(id: String): Boolean{
        return appDao.countById(id) > 0
    }

    suspend fun getAllTracks(): List<Track> {
        return appDao.getAllTracks().map {it.toTrack() }
    }

    suspend fun deleteTrackById(id: String){
        appDao.deleteTrackById(id)
    }

    suspend fun addQueryToHistory(searchHistory: SearchHistory){
        appDao.insertSearchHistory(searchHistory)
    }

    suspend fun getSearchHistoryList(query: String = ""): MutableLiveData<List<SearchHistory>> {
        return MutableLiveData(appDao.getSearchHistoryList(query))
    }

    suspend fun deleteSearchHistory(query: String){
        appDao.deleteSearchHistory(query)
    }
}