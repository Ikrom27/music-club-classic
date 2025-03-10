package ru.ikrom.database

import kotlinx.coroutines.flow.Flow
import ru.ikrom.database.db.AlbumDao
import ru.ikrom.database.db.ArtistDao
import ru.ikrom.database.db.PlaylistDao
import ru.ikrom.database.db.TracksDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val likedTracksDao: TracksDao,
    private val likedArtistDao: ArtistDao,
    private val likedAlbumDao: AlbumDao,
    private val playlistDao: PlaylistDao
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

    override suspend fun addTrackToPlaylist(playlistId: String, trackId: String) {
        playlistDao.insertTrackToPlaylist(
            PlaylistTrackCrossRef(
                playlistId = playlistId,
                trackId = trackId,
                position = System.currentTimeMillis()
            )
        )
    }

    override suspend fun getPlaylistTracks(playlistId: String): Flow<List<TrackEntity>> {
        return playlistDao.getTracksForPlaylist(playlistId)
    }

    override suspend fun getPlaylistById(playlistId: String): PlaylistEntity {
        return playlistDao.getPlaylistById(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: String, trackId: String) {
        playlistDao.removeTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun getSavedPlaylists(): Flow<List<PlaylistEntity>> {
        return playlistDao.getSavedPlaylists()
    }

    override suspend fun savePlaylist(playlist: PlaylistEntity) {
        playlistDao.savePlaylist(playlist)
    }
}

