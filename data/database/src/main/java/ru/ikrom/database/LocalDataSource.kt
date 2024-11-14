package ru.ikrom.database

import ru.ikrom.database.db.TracksDao
import javax.inject.Inject

internal class LocalDataSource @Inject constructor(
    private val likedTracksDao: TracksDao
) : ILocalDataSource {

    override suspend fun getAllTracks(): List<TrackEntity> {
        return likedTracksDao.getAllTracks()
    }

    override suspend fun isLikedTrack(id: String): Boolean {
        return likedTracksDao.isLikedTrack(id)
    }

    override suspend fun saveTrack(track: TrackEntity) {
        likedTracksDao.saveTrack(track)
    }

    override suspend fun removeTrack(track: TrackEntity) {
        likedTracksDao.removeTrack(track)
    }
}
