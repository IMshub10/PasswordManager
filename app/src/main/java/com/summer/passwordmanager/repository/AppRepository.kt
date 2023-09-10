package com.summer.passwordmanager.repository

import android.content.SharedPreferences
import android.os.Environment
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.summer.passwordmanager.beans.FileBean
import com.summer.passwordmanager.beans.toTagBeans
import com.summer.passwordmanager.beans.toTagEntities
import com.summer.passwordmanager.beans.toVaultBeans
import com.summer.passwordmanager.beans.toVaultEntities
import com.summer.passwordmanager.database.dao.AppDao
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.utils.AESEncryption
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.AppUtils.toFileBean
import com.summer.passwordmanager.utils.AppUtils.toJSON
import com.summer.passwordmanager.utils.EncryptionUtils
import com.summer.passwordmanager.utils.EncryptionUtils.toCharArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Base64

class AppRepository(
    private val dao: AppDao,
    private val sharedPreferences: SharedPreferences
) : Repository {

    override suspend fun insertIgnoreVaultEntity(vaultEntity: VaultEntity) =
        dao.insertVaultIgnore(vaultEntity)

    override suspend fun insertReplaceVaultEntity(vaultEntity: VaultEntity) =
        dao.insertVaultReplace(vaultEntity)

    override suspend fun insertPastHistory(passHistoryEntity: PassHistoryEntity) =
        dao.insertPassHistoryReplace(passHistoryEntity)

    override suspend fun save(fullName: String?, mobileNumber: String?) {
        sharedPreferences.edit()?.apply {
            putString(AppUtils.KEY_FULL_NAME, fullName).apply()
            putString(AppUtils.KEY_MOBILE_NUMBER, mobileNumber).apply()
        }
    }

    override suspend fun getFullName(): String? =
        sharedPreferences.getString(AppUtils.KEY_FULL_NAME, null)

    override suspend fun getMobileNumber(): String? =
        sharedPreferences.getString(AppUtils.KEY_MOBILE_NUMBER, null)

    override suspend fun getPin(): String? =
        sharedPreferences.getString(AppUtils.KEY_PIN, null)

    override suspend fun setPin(pin: String) {
        sharedPreferences.edit()?.apply {
            putString(AppUtils.KEY_PIN, pin).apply()
        }
    }

    override fun getAllTagsLive(): LiveData<List<TagEntity>?> =
        dao.getAllTagsLive()

    override fun getAllTags(): List<TagEntity> =
        dao.getAllTags()

    override fun getAllVaultsWithTheirTag(): LiveData<Map<VaultEntity, TagEntity?>> =
        dao.getAllVaultsWithTheirTag()

    override fun isFingerPrintSet(): Boolean =
        sharedPreferences.getBoolean(AppUtils.KEY_FINGERPRINT_ENABLED, false)

    override fun setFingerPrint(isFingerPrintSet: Boolean) {
        sharedPreferences.edit()?.apply {
            putBoolean(AppUtils.KEY_FINGERPRINT_ENABLED, isFingerPrintSet).apply()
        }
    }

    override suspend fun exportFile(appName: String, fileName: String, key: String): String? {
        var savedFileAbsolutePath: String? = null
        withContext(Dispatchers.IO) {
            try {
                val fileBean = FileBean(
                    tagBeans = getAllTags().toTagBeans(),
                    vaultBeans = getAllVaults().toVaultBeans()
                )
                val fileBeanJson = fileBean.toJSON()
                Log.d("TestingJson", fileBeanJson)
                val ivGen = EncryptionUtils.generateRandomBytes(16) //IV

                Log.d("TestingJson", "KeyGen $key")
                Log.d("TestingJson", "IVGen ${Base64.getEncoder().encodeToString(ivGen)}")
                val aesEncryption = AESEncryption(
                    key.toCharArray(),
                    ivGen.toCharArray()
                )
                val encryptedString = aesEncryption.encrypt(fileBeanJson)
                Log.d("TestingJson", "Encrypted $encryptedString")

                val path =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path + File.separator + appName
                File(path).mkdirs()
                val file = File(path, "${fileName}.txt")
                Log.d("path", path)
                if (file.exists())
                    file.delete()

                val fileStream = FileOutputStream(file)
                fileStream.bufferedWriter().use {
                    it.write(ivGen.toCharArray())
                    it.write("\n")
                    it.write(encryptedString)
                }
                savedFileAbsolutePath = file.absolutePath
                fileStream.flush()
                fileStream.close()

            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                e.printStackTrace()
            }
        }
        return savedFileAbsolutePath
    }

    override suspend fun importFile(inputStream: InputStream, key: String): Boolean {
        try {
            withContext(Dispatchers.IO) {
                val fileStream = BufferedReader(InputStreamReader(inputStream))
                val iiv = fileStream.readLine()
                Log.d("iv", String(iiv.toByteArray()))
                val decryptedString = AESEncryption(
                    key.toCharArray(),
                    iiv.toCharArray()
                ).decryptToString(
                    StringBuffer().apply {
                        fileStream.forEachLine {
                            append(it)
                        }
                    }.toString()
                )
                Log.d("TestingJson", "Decrypted $decryptedString")
                with(decryptedString.toFileBean()) {
                    dao.insertTagReplace(tagBeans.toTagEntities())
                    dao.insertVaultReplace(vaultBeans.toVaultEntities())
                }
                fileStream.close()
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
            return false
        }
        return true
    }

    override suspend fun deleteVaultById(vaultId: String) =
        dao.deleteVaultById(vaultId)

    override suspend fun deleteTagById(tagId: String) = dao.deleteTagById(tagId)

    override suspend fun insertReplaceTagEntity(tagEntity: TagEntity) =
        dao.insertTagReplace(tagEntity)

    override suspend fun getAllVaults() =
        dao.getAllVaults()

    override suspend fun getVaultById(vaultId: String) =
        dao.getVaultById(vaultId)

}