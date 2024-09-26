package ru.ikrom.youtube_data

import com.ikrom.innertube.models.AlbumItem
import com.ikrom.innertube.models.SongItem
import com.ikrom.innertube.models.WatchEndpoint
import com.ikrom.innertube.models.YTItem
import com.ikrom.innertube.pages.AlbumPage
import com.ikrom.innertube.pages.ArtistPage

interface IMediaDataSource {
    suspend fun getTracksByQuery(query: String): List<SongItem>
    suspend fun getNewReleaseAlbums(): List<AlbumItem>
    suspend fun getAlbumTracks(albumId: String): List<SongItem>
    suspend fun getRadioTracks(id: String): List<SongItem>
    suspend fun getPlaylistTracks(albumId: String): List<SongItem>
    suspend fun getArtistData(artistId: String): ArtistPage
    suspend fun getAlbumById(id: String): AlbumPage
}