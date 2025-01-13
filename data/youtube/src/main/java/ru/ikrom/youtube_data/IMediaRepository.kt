package ru.ikrom.youtube_data

import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.AlbumPageModel
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.ArtistPageModel
import ru.ikrom.youtube_data.model.TrackModel

interface IMediaRepository {
    suspend fun getArtistData(id: String): ArtistPageModel
    suspend fun getTracksByQuery(query: String): List<TrackModel>
    suspend fun getNewReleases(): List<AlbumModel>
    suspend fun getRadioTracks(id: String): List<TrackModel>
    suspend fun getAlbumTracks(albumId: String): List<TrackModel>
    suspend fun getPlaylistTracks(playlistId: String): List<TrackModel>
    suspend fun isFavorite(id: String): Boolean
    suspend fun isFavoriteArtist(id: String): Boolean
    suspend fun getAlbumPage(id: String): AlbumPageModel
    suspend fun getLikedTracks(): List<TrackModel>
    suspend fun saveTrack(id: String)
    suspend fun likeArtist(artistModel: ArtistModel)
    suspend fun unLikeArtist(artistModel: ArtistModel)
}