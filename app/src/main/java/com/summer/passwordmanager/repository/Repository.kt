package com.summer.passwordmanager.repository

import androidx.lifecycle.LiveData
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import java.io.InputStream

interface Repository {
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

    //region Preference methods
    suspend fun save(fullName: String?, mobileNumber: String?)
    suspend fun getFullName(): String?
    suspend fun getMobileNumber(): String?
    suspend fun getPin(): String?
    suspend fun setPin(pin: String)
    fun isFingerPrintSet(): Boolean
    fun setFingerPrint(isFingerPrintSet: Boolean)
    //endregion

    //region File methods
    suspend fun exportFile(appName: String, fileName: String, key: String): String?
    suspend fun importFile(inputStream: InputStream, key: String): Boolean
    //endregion
}