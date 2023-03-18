package com.summer.passwordmanager.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pass_history")
data class PassHistoryEntity(
    @PrimaryKey(autoGenerate = false) var id: String,
    var password: String,
    var hasLowerAlphas: Boolean,
    var hasUpperAlphas: Boolean,
    var hasNumbers: Boolean,
    var hasSpecialCharacters: Boolean,
    var createdAt: Long,
    var updatedAt: Long,
)