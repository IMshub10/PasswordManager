package com.summer.passwordmanager.repository

import androidx.lifecycle.LiveData
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.VaultEntity

interface Repository {
    //region room db methods
    suspend fun insertIgnoreVaultEntity(vaultEntity: VaultEntity)
    suspend fun insertReplaceVaultEntity(vaultEntity: VaultEntity)
    suspend fun insertPastHistory(passHistoryEntity: PassHistoryEntity)
    suspend fun insertReplaceTagEntity(tagEntity: TagEntity)
    fun getAllTagsLive(): LiveData<List<TagEntity>>
    fun getAllTags(): List<TagEntity>
    fun getAllVaultsWithTheirTag(): LiveData<Map<VaultEntity, TagEntity?>>
    suspend fun getAllVaults(): List<VaultEntity>
    suspend fun getVaultById(vaultId: String): VaultEntity?

    //endregion
    //region Preference methods
    suspend fun save(fullName: String?, mobileNumber: String?)
    suspend fun getFullName(): String?
    suspend fun getMobileNumber(): String?
    suspend fun getPin(): String?
    suspend fun setPin(pin: String)
    fun isFingerPrintSet(): Boolean
    fun setFingerPrint(isFingerPrintSet: Boolean)
    suspend fun deleteVaultById(vaultId: String)
    //endregion
}