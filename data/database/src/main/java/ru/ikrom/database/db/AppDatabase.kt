package ru.ikrom.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ikrom.database.AlbumEntity
import ru.ikrom.database.ArtistEntity
import ru.ikrom.database.CompanionDataEntity
import ru.ikrom.database.TrackEntity

@Database(entities = [TrackEntity::class, ArtistEntity::class, AlbumEntity::class, CompanionDataEntity::class], version = 4)
abstract class AppDatabase: RoomDatabase() {
    abstract fun likedTracksDao(): TracksDao
    abstract fun likedArtistsDao(): ArtistDao
    abstract fun likedAlbumDao(): AlbumDao
    abstract fun companionDao(): CompanionDao
}