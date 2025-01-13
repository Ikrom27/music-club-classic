package ru.ikrom.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ikrom.database.ArtistEntity
import ru.ikrom.database.TrackEntity

@Database(entities = [TrackEntity::class, ArtistEntity::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun likedTracksDao(): TracksDao
    abstract fun likedArtistsDao(): ArtistDao
}