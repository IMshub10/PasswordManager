package com.summer.passwordmanager.beans

import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity

fun TagEntity.toTagBean() =
    TagBean(
        id = id,
        createdAtApp = createdAtApp,
        updatedAtApp = updatedAtApp,
        name = name,
        description = description,
    )


fun VaultEntity.toVaultBean() =
    VaultBean(
        id = id,
        createdAtApp = createdAtApp,
        updatedAtApp = updatedAtApp,

        entityName = entityName,
        webAddress = webAddress,
        userNameMobileCardNumber = usernameMobileCardNumber,
        password = password,
        notes = notes,
        tagId = tagId
    )

fun List<VaultEntity>.toVaultBeans() = map { it.toVaultBean() }

fun List<TagEntity>.toTagBeans() = map { it.toTagBean() }
