package com.summer.passwordmanager.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "folders")
data class FolderEntity(
    @NotNull
    @PrimaryKey(autoGenerate = false) var id: String,
    @NotNull
    var name: String,
    var createdAt: Long,
    var updatedAt: Long,
)