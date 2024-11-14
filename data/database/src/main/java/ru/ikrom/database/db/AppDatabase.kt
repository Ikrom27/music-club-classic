package ru.ikrom.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ikrom.database.TrackEntity

@Database(entities = [TrackEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun likedTracksDao(): TracksDao
}