package ru.ikrom.database

interface ILocalDataSource {
    suspend fun getAllTracks(): List<TrackEntity>
    suspend fun isLikedTrack(id: String): Boolean
    suspend fun saveTrack(track: TrackEntity)
    suspend fun removeTrack(track: TrackEntity)
    suspend fun isLikedArtist(id: String): Boolean
    suspend fun saveArtist(artistEntity: ArtistEntity)
    suspend fun removeArtist(artistEntity: ArtistEntity)
}