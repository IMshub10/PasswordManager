package com.summer.passwordmanager.beans

import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity

fun TagEntity.toTagBean(): TagBean {
    return TagBean(
        id = id,
        name = name,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun VaultEntity.toVaultBean(): VaultBean {
    return VaultBean(
        id = id,
        entityName = entityName,
        webAddress = webAddress,
        userNameMobileCardNumber = userNameMobileCardNumber,
        password = password,
        notes = notes,
        tagId = tagId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun List<VaultEntity>.toVaultBeans(): List<VaultBean> {
    return this.map {
        it.toVaultBean()
    }
}

fun List<TagEntity>.toTagBeans(): List<TagBean> {
    return this.map {
        it.toTagBean()
    }
}
