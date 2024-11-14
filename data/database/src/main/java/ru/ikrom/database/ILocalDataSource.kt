package ru.ikrom.database

interface ILocalDataSource {
    suspend fun getAllTracks(): List<TrackEntity>
    suspend fun isLikedTrack(id: String): Boolean
    suspend fun saveTrack(track: TrackEntity)
    suspend fun removeTrack(track: TrackEntity)
}