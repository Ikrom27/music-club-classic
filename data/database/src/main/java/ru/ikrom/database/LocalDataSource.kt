package ru.ikrom.database

import ru.ikrom.database.db.ArtistDao
import ru.ikrom.database.db.TracksDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val likedTracksDao: TracksDao,
    private val likedArtistDao: ArtistDao
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

    override suspend fun isLikedArtist(id: String): Boolean {
        return likedArtistDao.isLikedArtist(id)
    }

    override suspend fun saveArtist(artistEntity: ArtistEntity) {
        return likedArtistDao.insertArtist(artistEntity)
    }

    override suspend fun removeArtist(artistEntity: ArtistEntity) {
        likedArtistDao.deleteArtist(artistEntity)
    }
}
