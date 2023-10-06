package com.summer.passwordmanager.database

data class EncryptWrapper(
    val iv: String,
    val key: String,
    val salt: String
)