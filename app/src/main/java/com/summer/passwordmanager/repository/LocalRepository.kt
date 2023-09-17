package com.summer.passwordmanager.repository

import androidx.lifecycle.LiveData
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity

interface LocalRepository {
    //region room db methods
    suspend fun insertIgnoreVaultEntity(vaultEntity: VaultEntity)
    suspend fun insertReplaceVaultEntity(vaultEntity: VaultEntity)
    suspend fun insertPastHistory(passHistoryEntity: PassHistoryEntity)
    suspend fun insertReplaceTagEntity(tagEntity: TagEntity)
    fun getAllTagsLive(): LiveData<List<TagEntity>?>
    fun getAllTags(): List<TagEntity>
    fun getAllVaultsWithTheirTag(): LiveData<Map<VaultEntity, TagEntity?>>
    suspend fun getAllVaults(): List<VaultEntity>
    suspend fun getVaultById(vaultId: String): VaultEntity?
    suspend fun deleteVaultById(vaultId: String)
    suspend fun deleteTagById(tagId: String)
    suspend fun getPassHistoryModel(id: String): PassHistoryEntity?
    //endregion
}