package ru.ikrom.database

import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {
    suspend fun getLikedTracks(): Flow<List<TrackEntity>>
    suspend fun getLikedArtists(): List<ArtistEntity>
    suspend fun isLikedTrack(id: String): Boolean
    suspend fun saveTrack(track: TrackEntity)
    suspend fun removeTrack(track: TrackEntity)
    suspend fun isLikedArtist(id: String): Boolean
    suspend fun saveArtist(artistEntity: ArtistEntity)
    suspend fun removeArtist(artistEntity: ArtistEntity)
}