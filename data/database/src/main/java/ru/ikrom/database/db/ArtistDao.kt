package ru.ikrom.database.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ikrom.database.ArtistEntity

@Dao
interface ArtistDao {
    @Query("select * from liked_artists")
    fun getAllArtists(): List<ArtistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: ArtistEntity)

    @Delete
    suspend fun deleteArtist(artist: ArtistEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM liked_artists WHERE id = :artistId)")
    suspend fun isLikedArtist(artistId: String): Boolean
}