package com.summer.passwordmanager.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey(autoGenerate = false) var id: String,
    var name: String,
    var createdAt: Long,
    var updatedAt: Long,
)