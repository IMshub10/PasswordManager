package com.summer.passwordmanager.repository

import com.summer.passwordmanager.database.dao.AppDao
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.VaultEntity

class AppRepository(private val dao: AppDao) : Repository {

    override suspend fun insertIgnoreVaultEntity(vaultEntity: VaultEntity) {
        dao.insertVaultIgnore(vaultEntity)
    }

    override suspend fun insertPastHistory(passHistoryEntity: PassHistoryEntity) {
        dao.insertPassHistoryReplace(passHistoryEntity)
    }

}