package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.beans.FileBean
import com.summer.passwordmanager.beans.toTagBeans
import com.summer.passwordmanager.beans.toTagEntity
import com.summer.passwordmanager.beans.toVaultBeans
import com.summer.passwordmanager.beans.toVaultEntity
import com.summer.passwordmanager.repository.FileRepository
import com.summer.passwordmanager.repository.LocalRepository
import com.summer.passwordmanager.repository.UserRepository
import com.summer.passwordmanager.ui.screens.main.models.UserModel
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import com.summer.passwordmanager.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val localRepository: LocalRepository,
    private val fileRepository: FileRepository,
) : ViewModel() {

    val userModel = UserModel()
    val key =
        TextEditTextModel(fieldType = TextEditTextFieldType.EXPORT_KEY, isRequired = true)

    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.Default) {
            val userName = userRepository.getFullName()
            val userMobile = userRepository.getMobileNumber()
            withContext(Dispatchers.Main) {
                userModel.apply {
                    name = userName.orEmpty()
                    mobile = userMobile.orEmpty()
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
    suspend fun exportFile(appName: String, fileOutputStream: OutputStream) {
        withContext(Dispatchers.IO) {
            fileRepository.exportFile(
                fileBean = FileBean(
                    localRepository.getAllTags().toTagBeans(),
                    vaultBeans = localRepository.getAllVaults().toVaultBeans()
                ),
                appName = appName,
                key = key.editTextContent!!,
                fileOutputStream = fileOutputStream
            )
        }
    }

    suspend fun importFile(inputStream: InputStream): Boolean {
        var result: Boolean
        withContext(Dispatchers.IO) {
            result = fileRepository.importFile(inputStream, key.editTextContent!!)
                .also { fileBean ->
                    fileBean?.tagBeans?.forEach {
                        localRepository.insertReplaceTagEntity(it.toTagEntity())
                    }
                    fileBean?.vaultBeans?.forEach {
                        localRepository.insertIgnoreVaultEntity(it.toVaultEntity())
                    }
                } != null
        }
        return result
    }
}