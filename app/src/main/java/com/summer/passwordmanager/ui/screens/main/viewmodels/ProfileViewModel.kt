package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.screens.main.models.UserModel
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import com.summer.passwordmanager.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

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
    suspend fun exportFile(appName: String) =
        repository.exportFile(appName, fileName.editTextContent!!, key.editTextContent!!)

    suspend fun importFile(inputStream: InputStream) =
        repository.importFile(inputStream, key.editTextContent!!)

}