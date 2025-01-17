package ru.ikrom.database.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.ikrom.database.AlbumEntity

@Dao
interface AlbumDao {
    @Query("SELECT * FROM liked_albums")
    fun getAllAlbums(): Flow<List<AlbumEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Delete
    suspend fun deleteAlbum(album: AlbumEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM liked_albums WHERE id = :albumId)")
    suspend fun isLikedAlbum(albumId: String): Boolean
}