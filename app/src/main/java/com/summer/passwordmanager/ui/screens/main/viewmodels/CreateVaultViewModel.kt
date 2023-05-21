package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.database.entities.FolderEntity
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel

class CreateVaultViewModel(private val repository: Repository) : ViewModel() {
    val websiteNameEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.VAULT_NAME, isRequired = true)
    val websiteAddressEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.WEBSITE_ADDRESS, isRequired = false)
    val userNameMobileEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.USERNAME_MOBILE, isRequired = false)
    val passwordEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.PASSWORD, isRequired = true)
    val notesEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.NOTES, isRequired = false)

    fun getAllFolders() :LiveData<List<FolderEntity>>{
        return repository.getAllFolders()
    }
}