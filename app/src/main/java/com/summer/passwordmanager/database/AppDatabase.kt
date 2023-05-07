package com.summer.passwordmanager.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.summer.passwordmanager.database.dao.AppDao
import com.summer.passwordmanager.database.entities.FolderEntity
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.VaultEntity

@Database(
    entities = [FolderEntity::class, VaultEntity::class, PassHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}