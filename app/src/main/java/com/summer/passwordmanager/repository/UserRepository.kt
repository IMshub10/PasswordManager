package com.summer.passwordmanager.repository

interface UserRepository {
    //region Preference methods
    suspend fun save(fullName: String?)
    suspend fun getFullName(): String?
    suspend fun getPin(): String?
    suspend fun setPin(pin: String)
    fun isFingerPrintSet(): Boolean
    fun setFingerPrint(isFingerPrintSet: Boolean)
    //endregion
}