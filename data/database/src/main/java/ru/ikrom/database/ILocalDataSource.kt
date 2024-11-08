package ru.ikrom.database

import androidx.lifecycle.LiveData

interface ILocalDataSource {
    fun getAllTracks(): LiveData<List<TrackEntity>>
    suspend fun isLikedTrack(id: String): Boolean
    suspend fun saveTrack(track: TrackEntity)
    suspend fun removeTrack(track: TrackEntity)
}