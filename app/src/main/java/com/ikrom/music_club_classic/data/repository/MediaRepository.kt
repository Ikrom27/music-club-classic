package com.ikrom.music_club_classic.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ikrom.innertube.YouTube
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.innertube.models.SearchSuggestions
import com.ikrom.innertube.models.WatchEndpoint
import com.ikrom.music_club_classic.data.data_source.account_data_source.AccountLocalDataSource
import com.ikrom.music_club_classic.data.data_source.IMediaDataSource
import com.ikrom.music_club_classic.data.data_source.media_data_source.LocalMediaDataSource
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.SearchHistory
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.toTrackEntity
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val youtubeService: IMediaDataSource,
    private val localMediaDataSource: LocalMediaDataSource,
    private val accountLocalDataSource: AccountLocalDataSource
) {
    init {
        val cookie = accountLocalDataSource.getCookie()
        YouTube.cookie = cookie
        Log.d("MusicServiceRepository", cookie)
    }

    fun getServerStatus(): MutableLiveData<Int> {
        return youtubeService.getServerStatus()
    }

    fun getTracksByQuery(query: String): MutableLiveData<List<Track>> {
        return youtubeService.getTracksByQuery(query)
    }

    fun getLikedPlayLists(): MutableLiveData<List<PlayList>>{
        return youtubeService.getLikedPlayLists()
    }

    fun getNewReleases(): MutableLiveData<List<Album>> {
        return youtubeService.getNewReleaseAlbums()
    }

    fun getRadioTracks(endpoint: WatchEndpoint): MutableLiveData<List<Track>>{
        return youtubeService.getRadioTracks(endpoint)
    }

    fun getAlbumTracks(albumId: String): MutableLiveData<List<Track>> {
        return youtubeService.getAlbumTracks(albumId)
    }

    fun getPlaylistTracks(albumId: String): MutableLiveData<List<Track>> {
        return youtubeService.getPlaylistTracks(albumId)
    }

    fun getSearchSuggestions(query: String): MutableLiveData<SearchSuggestions>{
        return youtubeService.getSearchSuggestions(query)
    }

    fun getTrackById(id: String): MutableLiveData<Track>{
        return youtubeService.getTrackById(id)
    }

    suspend fun addToSearchHistory(searchHistory: SearchHistory){
        localMediaDataSource.addQueryToHistory(searchHistory)
    }

    suspend fun getSearchHistoryList(query: String): MutableLiveData<List<SearchHistory>>{
        return localMediaDataSource.getSearchHistoryList(query)
    }

    suspend fun deleteFromSearchHistory(query: String) {
        localMediaDataSource.deleteSearchHistory(query)
    }

    suspend fun getFavoriteTracks(): List<Track>{
        return localMediaDataSource.getAllTracks()
    }

    suspend fun addToFavorite(track: Track){
        localMediaDataSource.insertTrack(track.toTrackEntity())
    }

    suspend fun isFavorite(id: String): Boolean{
        return localMediaDataSource.isFavorite(id)
    }

    suspend fun deleteTrackById(id: String){
        localMediaDataSource.deleteTrackById(id)
    }
}