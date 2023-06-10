package com.summer.passwordmanager.database.entities

import androidx.databinding.BaseObservable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.summer.passwordmanager.utils.AppUtils

@Entity(
    tableName = "vaults", foreignKeys = [ForeignKey(
        entity = TagEntity::class,
        childColumns = ["tagId"],
        parentColumns = ["id"],
        onDelete = ForeignKey.SET_NULL,
    )]
)
data class VaultEntity(
    @PrimaryKey(autoGenerate = false) var id: String = AppUtils.generateXid(),
    var entityName: String = "",
    var webAddress: String = "",
    var userNameMobileCardNumber: String = "",
    var password: String = "",
    var notes: String = "",
    var tagId: String? = null,
    var createdAt: Long = 0L,
    var updatedAt: Long = 0L,
) : BaseObservable() {
    @Ignore
    var tagEntity: TagEntity? = null

}