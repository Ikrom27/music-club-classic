package ru.ikrom.database

import androidx.lifecycle.LiveData
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val likedTracksDao: TracksDao
) : ILocalDataSource {

    override fun getAllTracks(): LiveData<List<TrackEntity>> {
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
