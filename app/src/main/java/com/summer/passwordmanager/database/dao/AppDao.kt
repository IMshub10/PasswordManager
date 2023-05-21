package com.summer.passwordmanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.summer.passwordmanager.database.entities.FolderEntity
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

    @Query("Select * from folders order by createdAt")
    fun getAllFolders(): LiveData<List<FolderEntity>>
}