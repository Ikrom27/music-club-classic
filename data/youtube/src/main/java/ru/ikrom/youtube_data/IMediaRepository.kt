package ru.ikrom.youtube_data

import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.ArtistPageModel
import ru.ikrom.youtube_data.model.PlaylistModel
import ru.ikrom.youtube_data.model.TrackModel

interface IMediaRepository {
    suspend fun getArtistData(id: String): ArtistPageModel
    suspend fun getTracksByQuery(query: String): List<TrackModel>
    suspend fun getNewReleases(): List<AlbumModel>
    suspend fun getRadioTracks(id: String): List<TrackModel>
    suspend fun getAlbumTracks(albumId: String): List<TrackModel>
    suspend fun getPlaylistTracks(playlistId: String): List<TrackModel>
    suspend fun isFavorite(id: String): Boolean
    suspend fun getAlbumById(id: String): AlbumModel
}