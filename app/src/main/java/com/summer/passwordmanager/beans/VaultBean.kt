package com.summer.passwordmanager.beans

import com.google.gson.annotations.SerializedName

data class VaultBean(
    @SerializedName("id")
    var id: String,
    @SerializedName("entity_name")
    var entityName: String,
    @SerializedName("web_address")
    var webAddress: String,
    @SerializedName("username_mobile_card_number")
    var userNameMobileCardNumber: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("notes")
    var notes: String,
    @SerializedName("tag_id")
    var tagId: String?,
    @SerializedName("created_at")
    var createdAt: Long,
    @SerializedName("updated_at")
    var updatedAt: Long,
)