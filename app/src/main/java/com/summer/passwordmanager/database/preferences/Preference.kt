package com.summer.passwordmanager.database.preferences

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


class Preference {

    companion object {
        private const val MASTER_KEY_ALIAS = "_androidx_security_master_key_"
        private const val APP_PREFERENCE_NAME = "password_manager_pref"

        private fun setupMasterKey(applicationContext: Context): MasterKey {
            return MasterKey.Builder(applicationContext)
                .setKeyGenParameterSpec(
                    KeyGenParameterSpec.Builder(
                        MASTER_KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                    )
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setKeySize(256)
                        .build()
                )
                .build()
        }

        fun createdEncryptedPreference(applicationContext: Context): SharedPreferences {
            return EncryptedSharedPreferences.create(
                applicationContext,
                APP_PREFERENCE_NAME,
                setupMasterKey(applicationContext),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        }

        //region Values
        const val KEY_FULL_NAME = "full_name"
        //const val KEY_MOBILE_NUMBER = "mobile_number"
        const val KEY_PIN = "pin"
        const val KEY_FINGERPRINT_ENABLED = "fingerprint_enabled"
        const val KEY_ADD = "add"
        const val KEY_ALL = "all"

        //DB
        const val KEY_DB_PASSCODE = "db_key"
        const val KEY_DB_WRAPPER_DATA = "db_wrapper_data"
        //endregion
    }

}