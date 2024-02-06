package com.ikrom.music_club_classic.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ikrom.music_club_classic.data.model.SearchHistory
import com.ikrom.music_club_classic.data.model.TrackEntity

@Database(entities = [TrackEntity::class, SearchHistory::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun dao(): AppDao
}
