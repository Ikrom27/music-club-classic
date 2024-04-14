package com.ikrom.music_club_classic.data.data_source.media_data_source

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.ikrom.innertube.YouTube
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.innertube.models.SearchSuggestions
import com.ikrom.innertube.models.SongItem
import com.ikrom.innertube.models.WatchEndpoint
import com.ikrom.music_club_classic.data.data_source.IMediaDataSource
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.PlayList
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.music_club_classic.extensions.toAlbum
import com.ikrom.music_club_classic.extensions.toPlayList
import com.ikrom.music_club_classic.extensions.toTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class YoutubeDataSource: IMediaDataSource {
    private var continuation = ""

    override fun getTracksByQuery(query: String): MutableLiveData<List<Track>> {
        val responseLiveData = MutableLiveData<List<Track>>(null)
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.search(query, YouTube.SearchFilter.FILTER_SONG).onSuccess { result ->
                val tracks = result.items.mapNotNull { (it as SongItem).toTrack() }
                responseLiveData.postValue(tracks)
            }.onFailure {
                Log.e(TAG, "onFailure error: $it")
            }
        }
        return responseLiveData
    }

    override fun getRadioTracks(endpoint: WatchEndpoint): MutableLiveData<List<Track>> {
        val responseLiveData = MutableLiveData<List<Track>>(ArrayList())
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.next(endpoint, continuation).onSuccess {result ->
                val tracks = result.items.mapNotNull { it.toTrack() }
                continuation = result.continuation ?: ""
                responseLiveData.postValue(tracks.drop(1))
            }
        }
        return responseLiveData
    }

    override fun getLikedPlayLists(): MutableLiveData<List<PlayList>> {
        val responseLiveData = MutableLiveData<List<PlayList>>(ArrayList())
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.likedPlaylists().onSuccess { result ->
                responseLiveData.postValue(result.map { it.toPlayList() })
            }.onFailure {
                Log.e(TAG, "onFailure error: $it")
            }
        }
        return responseLiveData
    }
    
    override fun getTrackById(id: String): MutableLiveData<Track> {
        val responseLiveData = MutableLiveData<Track>()
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.search(id, YouTube.SearchFilter.FILTER_SONG).onSuccess { result ->
                responseLiveData.postValue((result.items.first() as SongItem).toTrack())
            }.onFailure {
                Log.e(TAG, "onFailure error: $it")
            }
        }
        return responseLiveData
    }

    override fun getNewReleaseAlbums(): MutableLiveData<List<Album>> {
        val responseLiveData = MutableLiveData<List<Album>>(ArrayList())
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.newReleaseAlbums().onSuccess { result ->
                responseLiveData.postValue(result.map{ it.toAlbum() })
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
                responseLiveData.postValue(result.songs.map { it.toTrack()!! })
            }.onFailure {
                Log.e(TAG, "onFailure error: $it")
            }
        }
        return responseLiveData
    }

    override fun getPlaylistTracks(albumId: String): MutableLiveData<List<Track>> {
        val responseLiveData = MutableLiveData<List<Track>>(ArrayList())
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.playlist(albumId).onSuccess { result ->
                responseLiveData.postValue(result.songs.map { it.toTrack()!! })
            }.onFailure {
                Log.e(TAG, "onFailure error: $it")
            }
        }
        return responseLiveData
    }

    override fun getSearchSuggestions(query: String): MutableLiveData<SearchSuggestions> {
        val responseLiveData = MutableLiveData(SearchSuggestions(emptyList(), emptyList()))
        CoroutineScope(Dispatchers.IO).launch {
            YouTube.searchSuggestions(query).onSuccess { result ->
                responseLiveData.postValue(result)
            }
        }
        return responseLiveData
    }

    companion object {
        private val TAG = "YoutubeMusicDataSource"
    }
}