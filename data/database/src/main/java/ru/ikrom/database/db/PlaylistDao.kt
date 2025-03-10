package ru.ikrom.database.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.ikrom.database.PlaylistEntity
import ru.ikrom.database.PlaylistTrackCrossRef
import ru.ikrom.database.TrackEntity

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackToPlaylist(crossRef: PlaylistTrackCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun savePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM user_playlist")
    fun getSavedPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM user_playlist WHERE id == :playlistId")
    suspend fun getPlaylistById(playlistId: String): PlaylistEntity

    @Transaction
    @Query("SELECT * FROM liked_tracks WHERE id IN (SELECT track_id FROM playlist_tracks WHERE playlist_id = :playlistId)")
    suspend fun getTracksForPlaylist(playlistId: String): List<TrackEntity>

    @Query("DELETE FROM playlist_tracks WHERE playlist_id = :playlistId AND track_id = :trackId")
    suspend fun removeTrackFromPlaylist(playlistId: String, trackId: String)
}