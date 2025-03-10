package ru.ikrom.youtube_data

import kotlinx.coroutines.flow.Flow
import ru.ikrom.youtube_data.model.AlbumModel
import ru.ikrom.youtube_data.model.AlbumPageModel
import ru.ikrom.youtube_data.model.ArtistModel
import ru.ikrom.youtube_data.model.ArtistPageModel
import ru.ikrom.youtube_data.model.PlaylistModel
import ru.ikrom.youtube_data.model.PlaylistPageModel
import ru.ikrom.youtube_data.model.TrackModel

interface IMediaRepository {
    suspend fun getArtistData(id: String): ArtistPageModel
    suspend fun getTracksByQuery(query: String): List<TrackModel>
    suspend fun getNewReleases(): List<AlbumModel>
    suspend fun getRadioTracks(id: String): List<TrackModel>
    suspend fun getAlbumTracks(albumId: String): List<TrackModel>
    suspend fun getPlaylistTracks(playlistId: String): Flow<List<TrackModel>>
    suspend fun isFavorite(id: String): Boolean
    suspend fun isFavoriteArtist(id: String): Boolean
    suspend fun getAlbumPage(id: String): AlbumPageModel
    suspend fun getPlaylistPage(id: String): PlaylistPageModel
    suspend fun getLikedTracks(): Flow<List<TrackModel>>
    suspend fun getLikedArtists(): Flow<List<ArtistModel>>
    suspend fun saveTrack(id: String)
    suspend fun likeArtist(artistModel: ArtistModel)
    suspend fun unLikeArtist(artistModel: ArtistModel)
    suspend fun likeAlbum(albumModel: AlbumModel)
    suspend fun unLikeAlbum(albumModel: AlbumModel)
    suspend fun unLikeTrack(track: TrackModel)
    suspend fun isFavoriteAlbum(albumId: String): Boolean
    suspend fun getLikedAlbums(): Flow<List<AlbumModel>>
    suspend fun getSavedPlaylists(): Flow<List<PlaylistModel>>
    suspend fun savePlaylist(playlistModel: PlaylistModel)
    suspend fun addTrackToPlaylist(playlistId: String, trackId: String)
}