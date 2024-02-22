package com.ikrom.music_club_classic.data.data_source

import androidx.lifecycle.MutableLiveData
import com.ikrom.innertube.models.PlaylistItem
import com.ikrom.music_club_classic.data.model.Album
import com.ikrom.music_club_classic.data.model.Track
import com.ikrom.innertube.models.SearchSuggestions

interface IMediaDataSource {
    fun getTracksByQuery(query: String): MutableLiveData<List<Track>>
    fun getNewReleaseAlbums(): MutableLiveData<List<Album>>
    fun getAlbumTracks(albumId: String): MutableLiveData<List<Track>>
    fun getSearchSuggestions(query: String): MutableLiveData<SearchSuggestions>
    fun getTrackById(id: String): MutableLiveData<Track>
    fun getLikedPlayLists(): MutableLiveData<List<PlaylistItem>>
}