package com.summer.passwordmanager.beans

import com.google.gson.annotations.SerializedName

data class TagBean(
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("created_at")
    var createdAt: Long,
    @SerializedName("updated_at")
    var updatedAt: Long,
)