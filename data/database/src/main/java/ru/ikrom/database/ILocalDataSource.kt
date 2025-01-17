package ru.ikrom.database

import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    suspend fun getLikedTracks(): Flow<List<TrackEntity>>
    suspend fun getLikedArtists(): Flow<List<ArtistEntity>>
    suspend fun getLikedAlbums(): Flow<List<AlbumEntity>>
    suspend fun isLikedTrack(id: String): Boolean
    suspend fun isLikedAlbum(id: String): Boolean
    suspend fun saveTrack(track: TrackEntity)
    suspend fun saveAlbum(album: AlbumEntity)
    suspend fun removeTrack(track: TrackEntity)
    suspend fun removeAlbum(album: AlbumEntity)
    suspend fun isLikedArtist(id: String): Boolean
    suspend fun saveArtist(artistEntity: ArtistEntity)
    suspend fun removeArtist(artistEntity: ArtistEntity)
}
