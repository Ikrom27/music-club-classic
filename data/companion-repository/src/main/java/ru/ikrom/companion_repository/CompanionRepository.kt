package ru.ikrom.companion_repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.ikrom.database.CompanionDataEntity
import ru.ikrom.database.db.CompanionDao
import javax.inject.Inject

class CompanionRepository @Inject constructor(
    private val dao: CompanionDao
) {
    suspend fun saveCompanionData(data: String) = withContext(Dispatchers.IO) {
        dao.saveCompanionData(CompanionDataEntity(data))
    }

    suspend fun getSavedCompanionData(): List<CompanionDataEntity> {
        return dao.getSavedCompanionData()
    }
}