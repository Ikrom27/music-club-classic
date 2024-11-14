package ru.ikrom.database.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ikrom.database.TrackEntity

@Dao
interface TracksDao {
    @Query("select * from liked_tracks")
    fun getAllTracks(): List<TrackEntity>

    @Query("SELECT COUNT(*) > 0 FROM liked_tracks WHERE id = :id")
    fun isLikedTrack(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTrack(track: TrackEntity)

    @Delete
    fun removeTrack(track: TrackEntity)
}