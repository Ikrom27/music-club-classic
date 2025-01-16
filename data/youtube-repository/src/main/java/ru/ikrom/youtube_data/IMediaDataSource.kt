package ru.ikrom.youtube_data

import com.zionhuang.innertube.models.AlbumItem
import com.zionhuang.innertube.models.SongItem
import com.zionhuang.innertube.pages.AlbumPage
import com.zionhuang.innertube.pages.ArtistPage


interface IMediaDataSource {
    suspend fun getTracksByQuery(query: String): List<SongItem>
    suspend fun getNewReleaseAlbums(): List<AlbumItem>
    suspend fun getAlbumTracks(albumId: String): List<SongItem>
    suspend fun getPlaylistTracks(albumId: String): List<SongItem>
    suspend fun getArtistData(artistId: String): ArtistPage
    suspend fun getAlbumPage(id: String): AlbumPage
    suspend fun getRadioTracks(id: String, continuation: String = ""): List<SongItem>
}