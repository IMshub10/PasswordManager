package com.summer.passwordmanager.database.entities

import androidx.databinding.BaseObservable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = false)
    var id: String,
    var name: String,
    var description: String,
    var createdAt: Long,
    var updatedAt: Long,
) : BaseObservable() {

    @Ignore
    var isSelected = false
}