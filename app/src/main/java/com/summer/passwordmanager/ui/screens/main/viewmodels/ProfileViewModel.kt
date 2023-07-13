package com.summer.passwordmanager.ui.screens.main.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.App
import com.summer.passwordmanager.beans.FileBean
import com.summer.passwordmanager.beans.toTagBeans
import com.summer.passwordmanager.beans.toVaultBeans
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.screens.main.models.UserModel
import com.summer.passwordmanager.utils.AESEncryption
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.AppUtils.toJSON
import com.summer.passwordmanager.utils.EncryptionUtils
import com.summer.passwordmanager.utils.EncryptionUtils.toCharArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Base64

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    val userModel = UserModel()

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

    /**
     * Saves in Documents/{App_name}/encrypted File/file_name.txt
     */
    fun exportFile() {
        viewModelScope.launch(Dispatchers.IO) {
            val fileBean = FileBean(
                tagBeans = repository.getAllTags().toTagBeans(),
                vaultBeans = repository.getAllVaults().toVaultBeans()
            )
            val fileBeanJson = fileBean.toJSON()
            Log.d("TestingJson", fileBeanJson)
            val keyGen = AESEncryption.keyGen() //Password
            val ivGen = EncryptionUtils.generateRandomBytes(16) //IV

            Log.d("TestingJson", "KeyGen ${Base64.getEncoder().encodeToString(keyGen.encoded)}")
            Log.d("TestingJson", "IVGen ${Base64.getEncoder().encodeToString(ivGen)}")
            val aesEncryption = AESEncryption(
                keyGen.encoded.toCharArray(),
                ivGen.toCharArray()
            )
            val encryptedString = aesEncryption.encrypt(fileBeanJson)
            Log.d("TestingJson", "Encrypted $encryptedString")
            val decryptedString = aesEncryption.decryptToString(encryptedString)
            Log.d("TestingJson", "Decrypted $decryptedString")
        }
    }
}