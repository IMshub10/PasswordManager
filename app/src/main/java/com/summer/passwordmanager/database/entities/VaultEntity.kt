package com.summer.passwordmanager.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaults")
data class VaultEntity(
    @PrimaryKey(autoGenerate = false) var id: String,
    var name: String,
    var userName: String,
    var password: String,
    var url: String,
    var createdAt:Long,
    var updatedAt:Long,
)