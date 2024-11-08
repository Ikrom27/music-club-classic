package ru.ikrom.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TrackEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun likedTracksDao(): TracksDao
}