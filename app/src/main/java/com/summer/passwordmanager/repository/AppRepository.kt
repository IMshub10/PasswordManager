package com.summer.passwordmanager.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.summer.passwordmanager.database.dao.AppDao
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.utils.AppUtils

class AppRepository(
    private val dao: AppDao,
    private val sharedPreferences: SharedPreferences
) : Repository {

    override suspend fun insertIgnoreVaultEntity(vaultEntity: VaultEntity) {
        dao.insertVaultIgnore(vaultEntity)
    }

    override suspend fun insertPastHistory(passHistoryEntity: PassHistoryEntity) {
        dao.insertPassHistoryReplace(passHistoryEntity)
    }

    override suspend fun save(fullName: String?, mobileNumber: String?) {
        sharedPreferences.edit()?.apply {
            putString(AppUtils.KEY_FULL_NAME, fullName).apply()
            putString(AppUtils.KEY_MOBILE_NUMBER, mobileNumber).apply()
        }
    }

    override suspend fun getFullName(): String? {
        return sharedPreferences.getString(AppUtils.KEY_FULL_NAME, null)
    }

    override suspend fun getMobileNumber(): String? {
        return sharedPreferences.getString(AppUtils.KEY_MOBILE_NUMBER, null)
    }

    override suspend fun getPin(): String? {
        return sharedPreferences.getString(AppUtils.KEY_PIN, null)
    }

    override suspend fun setPin(pin: String) {
        sharedPreferences.edit()?.apply {
            putString(AppUtils.KEY_PIN, pin).apply()
        }
    }

    override fun getAllTags(): LiveData<List<TagEntity>> {
        return dao.getAllTags()
    }

    override fun getAllVaultsWithTheirTag(): LiveData<Map<VaultEntity, TagEntity?>> {
        return dao.getAllVaultsWithTheirTag()
    }

    override fun isFingerPrintSet(): Boolean {
        return sharedPreferences.getBoolean(AppUtils.KEY_FINGERPRINT_ENABLED, false)
    }

    override fun setFingerPrint(isFingerPrintSet: Boolean) {
        sharedPreferences.edit()?.apply {
            putBoolean(AppUtils.KEY_FINGERPRINT_ENABLED, isFingerPrintSet).apply()
        }
    }

}