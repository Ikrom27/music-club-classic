package ru.ikrom.database.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ikrom.database.CompanionDataEntity

@Dao
interface CompanionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCompanionData(companionDataEntity: CompanionDataEntity)
    @Query("select * from companion_data")
    suspend fun getSavedCompanionData(): List<CompanionDataEntity>
}