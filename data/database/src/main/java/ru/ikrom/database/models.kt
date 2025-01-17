package ru.ikrom.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "liked_tracks")
data class TrackEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "artist") val artist: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "album_id") val albumId: String,
    @ColumnInfo(name = "artist_id") val artistId: String,
    @ColumnInfo(name = "is_downloaded") val isDownloaded: Boolean,
)

@Entity(tableName = "liked_artists")
data class ArtistEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")val id: String,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "thumbnail")val thumbnail: String? = null
)

@Entity(tableName = "liked_albums")
data class AlbumEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "artist_names") val artistNames: String,
    @ColumnInfo(name = "artist_id") val artistId: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String,
    @ColumnInfo(name = "year") val year: Int?
)