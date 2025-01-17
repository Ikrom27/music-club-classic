package ru.ikrom.database

import kotlinx.coroutines.flow.Flow
import ru.ikrom.database.db.AlbumDao
import ru.ikrom.database.db.ArtistDao
import ru.ikrom.database.db.TracksDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val likedTracksDao: TracksDao,
    private val likedArtistDao: ArtistDao,
    private val likedAlbumDao: AlbumDao
) : ILocalDataSource {

    override suspend fun getLikedTracks(): Flow<List<TrackEntity>> {
        return likedTracksDao.getAllTracks()
    }

    override suspend fun getLikedArtists(): Flow<List<ArtistEntity>> {
        return likedArtistDao.getAllArtists()
    }

    override suspend fun getLikedAlbums(): Flow<List<AlbumEntity>> {
        return likedAlbumDao.getAllAlbums()
    }

    override suspend fun isLikedTrack(id: String): Boolean {
        return likedTracksDao.isLikedTrack(id)
    }

    override suspend fun isLikedAlbum(id: String): Boolean {
        return likedAlbumDao.isLikedAlbum(id)
    }

    override suspend fun saveTrack(track: TrackEntity) {
        likedTracksDao.saveTrack(track)
    }

    override suspend fun saveAlbum(album: AlbumEntity) {
        likedAlbumDao.insertAlbum(album)
    }

    override suspend fun removeTrack(track: TrackEntity) {
        likedTracksDao.removeTrack(track)
    }

    override suspend fun removeAlbum(album: AlbumEntity) {
        likedAlbumDao.deleteAlbum(album)
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

