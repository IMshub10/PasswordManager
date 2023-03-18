package com.summer.passwordmanager.repository

import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.VaultEntity

interface Repository {
    //region room db methods
    suspend fun insertIgnoreVaultEntity(vaultEntity: VaultEntity)
    suspend fun insertPastHistory(passHistoryEntity: PassHistoryEntity)
    //endregion
}