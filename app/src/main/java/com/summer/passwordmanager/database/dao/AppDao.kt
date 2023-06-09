package com.summer.passwordmanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.VaultEntity

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertVaultIgnore(vaultEntity: VaultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVaultReplace(vaultEntity: VaultEntity)

    @Update
    fun updateVault(vaultEntity: VaultEntity)

    @Query("Delete From vaults where id =:id ")
    fun deleteVaultById(id: String)

    @Query("Delete From vaults")
    fun deleteAllVaults()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPassHistoryIgnore(vaultEntity: VaultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPassHistoryReplace(passHistoryEntity: PassHistoryEntity)

    @Query("Delete From pass_history")
    fun deleteAllPassHistoryRecords()

    @Query("Select * from tags order by lower(name)")
    fun getAllTagsLive(): LiveData<List<TagEntity>>

    @Query("Select * from tags order by lower(name)")
    fun getAllTags(): List<TagEntity>

    @Query(
        "Select vaults.*, tags.* from vaults " +
                " left join tags on tags.id = vaults.tagId " +
                " order by vaults.entityName, vaults.createdAt "
    )
    fun getAllVaultsWithTheirTag(): LiveData<Map<VaultEntity, TagEntity?>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTagIgnore(vaultEntity: TagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTagReplace(vaultEntity: TagEntity)
}