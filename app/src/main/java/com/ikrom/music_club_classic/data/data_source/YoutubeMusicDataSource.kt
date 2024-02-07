package com.ikrom.music_club_classic.data.data_source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ikrom.innertube.YouTube
import com.ikrom.innertube.models.SearchSuggestions
import com.ikrom.innertube.models.SongItem
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.toAlbum
import com.ikrom.music_club_classic.extensions.toTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class YoutubeMusicDataSource: IMusicServiceDataSource {
    private val TAG = "YoutubeMusicDataSource"
    override fun getTracksByQuery(query: String): MutableLiveData<List<Track>> {
        val tracksLiveData = MutableLiveData<List<Track>>(ArrayList())
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.search(query, YouTube.SearchFilter.FILTER_SONG).onSuccess { result ->
                tracksLiveData.postValue(result.items.map { (it as SongItem).toTrack()})
                Log.d("YoutubeMusicDataSource", "Find results: ${tracksLiveData.value?.size}")
            }.onFailure {
                Log.e(TAG, "onFailure error: $it")
            }
        }
        return tracksLiveData
    }

    override fun getNewReleaseAlbums(): MutableLiveData<List<Album>> {
        val responseLiveData = MutableLiveData<List<Album>>(ArrayList())
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.newReleaseAlbums().onSuccess { result ->
                responseLiveData.value = result.map{ it.toAlbum() }
            }.onFailure {
                Log.e(TAG, "onFailure error: $it")
            }
        }
        return responseLiveData
    }

    override fun getAlbumTracks(albumId: String): MutableLiveData<List<Track>> {
        val responseLiveData = MutableLiveData<List<Track>>(ArrayList())
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.album(albumId).onSuccess { result ->
                responseLiveData.value = result.songs.map { it.toTrack() }
            }.onFailure {
                Log.e(TAG, "get album ${albumId} tracks failure")
            }
        }
        return responseLiveData
    }

    override fun getSearchSuggestions(query: String): MutableLiveData<SearchSuggestions> {
        val responseLiveData = MutableLiveData(SearchSuggestions(emptyList(), emptyList()))
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.searchSuggestions(query).onSuccess { result ->
                responseLiveData.value = result
            }
        }
        return responseLiveData
    }
}