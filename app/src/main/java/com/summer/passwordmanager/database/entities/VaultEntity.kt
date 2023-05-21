package com.summer.passwordmanager.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaults", foreignKeys = [ForeignKey(
        entity = FolderEntity::class,
        childColumns = ["folderId"],
        parentColumns = ["id"],
        onDelete = ForeignKey.SET_NULL,
    )]
)
data class VaultEntity(
    @PrimaryKey(autoGenerate = false) var id: String,
    var entityName: String,
    var webAddress: String,
    var userNameMobileCardNumber: String,
    var password: String,
    var notes: String,
    var folderId: String? = null,
    var createdAt: Long,
    var updatedAt: Long,
)