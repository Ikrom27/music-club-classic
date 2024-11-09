package ru.ikrom.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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