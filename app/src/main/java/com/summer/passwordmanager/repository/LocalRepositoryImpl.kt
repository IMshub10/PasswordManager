package com.summer.passwordmanager.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.room.Dao
import com.summer.passwordmanager.database.dao.AppDao
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.utils.AppUtils

class LocalRepositoryImpl(private val dao: AppDao) : LocalRepository {
    override suspend fun insertIgnoreVaultEntity(vaultEntity: VaultEntity) =
        dao.insertVaultIgnore(vaultEntity)

    override suspend fun insertReplaceVaultEntity(vaultEntity: VaultEntity) =
        dao.insertVaultReplace(vaultEntity)

    override suspend fun insertPastHistory(passHistoryEntity: PassHistoryEntity) =
        dao.insertPassHistoryReplace(passHistoryEntity)


    override fun getAllTagsLive(): LiveData<List<TagEntity>?> =
        dao.getAllTagsLive()

    override fun getAllTags(): List<TagEntity> =
        dao.getAllTags()

    override fun getAllVaultsWithTheirTag(): LiveData<Map<VaultEntity, TagEntity?>> =
        dao.getAllVaultsWithTheirTag()


    override suspend fun deleteVaultById(vaultId: String) =
        dao.deleteVaultById(vaultId)

    override suspend fun deleteTagById(tagId: String) = dao.deleteTagById(tagId)

    override suspend fun getPassHistoryModel(id: String) = dao.getPassHistoryModelById(id)

    override suspend fun insertReplaceTagEntity(tagEntity: TagEntity) =
        dao.insertTagReplace(tagEntity)

    override suspend fun getAllVaults() =
        dao.getAllVaults()

    override suspend fun getVaultById(vaultId: String) =
        dao.getVaultById(vaultId)
}