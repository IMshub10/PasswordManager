package com.summer.passwordmanager.ui.screens.main.viewmodels

import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.beans.FileBean
import com.summer.passwordmanager.beans.toTagBeans
import com.summer.passwordmanager.beans.toVaultBeans
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.screens.main.models.UserModel
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import com.summer.passwordmanager.utils.AESEncryption
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.AppUtils.toJSON
import com.summer.passwordmanager.utils.EncryptionUtils
import com.summer.passwordmanager.utils.EncryptionUtils.toCharArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Base64

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    val userModel = UserModel()
    val fileName =
        TextEditTextModel(fieldType = TextEditTextFieldType.EXPORT_FILE_NAME, isRequired = true)
    val key =
        TextEditTextModel(fieldType = TextEditTextFieldType.EXPORT_KEY, isRequired = true)

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val userName = repository.getFullName()
            val userMobile = repository.getMobileNumber()
            withContext(Dispatchers.Main) {
                userModel.apply {
                    name = userName ?: ""
                    mobile = userMobile ?: ""
                    notifyChange()
                }
            }
        }
    }

    fun generateKey() {
        key.editTextContent = AppUtils.getRandomString(32)
        key.notifyChange()
    }

    /**
     * Saves in Documents/{App_name}/encrypted File/file_name.txt
     */
    fun exportFile(appName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val fileBean = FileBean(
                tagBeans = repository.getAllTags().toTagBeans(),
                vaultBeans = repository.getAllVaults().toVaultBeans()
            )
            val fileBeanJson = fileBean.toJSON()
            Log.d("TestingJson", fileBeanJson)
            val ivGen = EncryptionUtils.generateRandomBytes(16) //IV

            Log.d("TestingJson", "KeyGen ${key.editTextContent ?: ""}")
            Log.d("TestingJson", "IVGen ${Base64.getEncoder().encodeToString(ivGen)}")
            val aesEncryption = AESEncryption(
                key.editTextContent?.toCharArray() ?: "".toCharArray(),
                ivGen.toCharArray()
            )
            val encryptedString = aesEncryption.encrypt(fileBeanJson)
            Log.d("TestingJson", "Encrypted $encryptedString")

            val path =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path + File.separator + appName
            File(path).mkdirs()
            val file = File(path, "${fileName.editTextContent ?: ""}.txt")
            Log.d("path", path)
            if (file.exists())
                file.delete()

            val fileStream = FileOutputStream(file)
            fileStream.bufferedWriter().use {
                it.write(ivGen.toCharArray())
                it.write("\n")
                it.write(encryptedString)
            }
            fileStream.flush()
            fileStream.close()
        }
    }

    fun importFile(inputStream: InputStream) {
        viewModelScope.launch(Dispatchers.IO) {
            val fileStream = BufferedReader(InputStreamReader(inputStream))
            val iiv = fileStream.readLine()
            Log.d("iv", String(iiv.toByteArray()))
            if (key.editTextContent != null) {
                val aesEncryption = AESEncryption(
                    key.editTextContent!!.toCharArray(),
                    iiv.toCharArray()
                )
                val content = StringBuffer()
                fileStream.forEachLine {
                    content.append(it)
                }
                val decryptedString = aesEncryption.decryptToString(content.toString())
                Log.d("TestingJson", "Decrypted $decryptedString")
            }
            fileStream.close()
        }
    }
}