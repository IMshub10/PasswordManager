package com.summer.passwordmanager.repository

import android.content.SharedPreferences
import com.summer.passwordmanager.database.preferences.Preference.Companion.KEY_FULL_NAME
import com.summer.passwordmanager.database.preferences.Preference.Companion.KEY_MOBILE_NUMBER
import com.summer.passwordmanager.database.preferences.Preference.Companion.KEY_PIN
import com.summer.passwordmanager.database.preferences.Preference.Companion.KEY_FINGERPRINT_ENABLED

class UserRepositoryImpl(private val sharedPreferences: SharedPreferences) : UserRepository {
    override suspend fun save(fullName: String?, mobileNumber: String?) {
        sharedPreferences.edit()?.apply {
            putString(KEY_FULL_NAME, fullName).apply()
            putString(KEY_MOBILE_NUMBER, mobileNumber).apply()
        }
    }

    override suspend fun getFullName(): String? =
        sharedPreferences.getString(KEY_FULL_NAME, null)

    override suspend fun getMobileNumber(): String? =
        sharedPreferences.getString(KEY_MOBILE_NUMBER, null)

    override suspend fun getPin(): String? =
        sharedPreferences.getString(KEY_PIN, null)

    override suspend fun setPin(pin: String) {
        sharedPreferences.edit()?.apply {
            putString(KEY_PIN, pin).apply()
        }
    }

    override fun isFingerPrintSet(): Boolean =
        sharedPreferences.getBoolean(KEY_FINGERPRINT_ENABLED, false)

    override fun setFingerPrint(isFingerPrintSet: Boolean) {
        sharedPreferences.edit()?.apply {
            putBoolean(KEY_FINGERPRINT_ENABLED, isFingerPrintSet).apply()
        }
    }

}