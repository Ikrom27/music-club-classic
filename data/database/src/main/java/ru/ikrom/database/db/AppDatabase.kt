package ru.ikrom.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ikrom.database.AlbumEntity
import ru.ikrom.database.ArtistEntity
import ru.ikrom.database.TrackEntity

@Database(entities = [TrackEntity::class, ArtistEntity::class, AlbumEntity::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun likedTracksDao(): TracksDao
    abstract fun likedArtistsDao(): ArtistDao
    abstract fun likedAlbumDao(): AlbumDao
}