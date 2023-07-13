package com.summer.passwordmanager.beans

import com.google.gson.annotations.SerializedName

data class FileBean(
    @SerializedName("tags")
    var tagBeans: List<TagBean>,
    @SerializedName("vaults")
    var vaultBeans: List<VaultBean>
)