package com.ikrom.music_club_classic.data.repository

import androidx.lifecycle.MutableLiveData
import com.ikrom.innertube.models.SearchSuggestions
import com.ikrom.music_club_classic.data.data_source.IMusicServiceDataSource
import com.ikrom.music_club_classic.data.data_source.LocalDataSource
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.SearchHistory
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.toTrackEntity
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MusicServiceRepository @Inject constructor(
    private val youtubeService: IMusicServiceDataSource,
    private val localDataSource: LocalDataSource
) {
    fun getTracksByQuery(query: String): MutableLiveData<List<Track>> {
        return youtubeService.getTracksByQuery(query)
    }

    fun getNewReleases(): MutableLiveData<List<Album>> {
        return youtubeService.getNewReleaseAlbums()
    }

    fun getAlbumTracks(albumId: String): MutableLiveData<List<Track>> {
        return youtubeService.getAlbumTracks(albumId)
    }

    fun getSearchSuggestions(query: String): MutableLiveData<SearchSuggestions>{
        return youtubeService.getSearchSuggestions(query)
    }

    fun getTrackById(id: String): MutableLiveData<Track>{
        return youtubeService.getTrackById(id)
    }

    suspend fun addToSearchHistory(searchHistory: SearchHistory){
        localDataSource.addQueryToHistory(searchHistory)
    }

    suspend fun getSearchHistoryList(query: String): MutableLiveData<List<SearchHistory>>{
        return localDataSource.getSearchHistoryList(query)
    }

    suspend fun deleteFromSearchHistory(query: String) {
        localDataSource.deleteSearchHistory(query)
    }

    suspend fun getFavoriteTracks(): List<Track>{
        return localDataSource.getAllTracks()
    }

    suspend fun addToFavorite(track: Track){
        localDataSource.insertTrack(track.toTrackEntity())
    }

    suspend fun isFavorite(id: String): Boolean{
        return localDataSource.isFavorite(id)
    }

    suspend fun deleteTrackById(id: String){
        localDataSource.deleteTrackById(id)
    }
}