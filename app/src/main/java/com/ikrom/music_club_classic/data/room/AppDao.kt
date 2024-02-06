package com.ikrom.music_club_classic.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ikrom.music_club_classic.data.model.SearchHistory
import com.ikrom.music_club_classic.data.model.TrackEntity

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(searchHistory: SearchHistory)
    @Query("SELECT * FROM SearchHistory WHERE SearchHistory.`query` LIKE :query || '%' ORDER BY SearchHistory.date")
    suspend fun getSearchHistoryList(query: String): List<SearchHistory>

    @Query("DELETE FROM SearchHistory WHERE `query` = :query")
    suspend fun deleteSearchHistory(query: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks WHERE videoId = :videoId")
    suspend fun getTrackById(videoId: String): TrackEntity

    @Query("SELECT COUNT(*) FROM tracks WHERE videoId = :videoId")
    suspend fun countById(videoId: String): Int

    @Query("SELECT * FROM tracks")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("DELETE FROM tracks WHERE videoId = :videoId")
    suspend fun deleteTrackById(videoId: String)
}