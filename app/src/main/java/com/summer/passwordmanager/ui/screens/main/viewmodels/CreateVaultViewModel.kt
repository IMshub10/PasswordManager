package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import com.summer.passwordmanager.utils.AppUtils

class CreateVaultViewModel(private val repository: Repository) : ViewModel() {

    var selectedFolderId: String? = null

    var websiteNameEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.VAULT_NAME, isRequired = true)
    var websiteAddressEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.WEBSITE_ADDRESS, isRequired = false)
    var userNameMobileEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.USERNAME_MOBILE, isRequired = true)
    var passwordEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.PASSWORD, isRequired = true)
    var notesEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.NOTES, isRequired = false)

    fun resetUiModels() {
        selectedFolderId = null
        websiteNameEditTextModel =
            TextEditTextModel(fieldType = TextEditTextFieldType.VAULT_NAME, isRequired = true)
        websiteAddressEditTextModel =
            TextEditTextModel(fieldType = TextEditTextFieldType.WEBSITE_ADDRESS, isRequired = false)
        userNameMobileEditTextModel =
            TextEditTextModel(fieldType = TextEditTextFieldType.USERNAME_MOBILE, isRequired = true)
        passwordEditTextModel =
            TextEditTextModel(fieldType = TextEditTextFieldType.PASSWORD, isRequired = true)
        notesEditTextModel =
            TextEditTextModel(fieldType = TextEditTextFieldType.NOTES, isRequired = false)
    }

    fun getAllTags(): LiveData<List<TagEntity>> {
        return repository.getAllTags()
    }

    suspend fun save() {
        repository.insertIgnoreVaultEntity(
            VaultEntity(
                id = AppUtils.generateXid(),
                entityName = websiteNameEditTextModel.editTextContent ?: "",
                userNameMobileCardNumber = userNameMobileEditTextModel.editTextContent ?: "",
                password = passwordEditTextModel.editTextContent ?: "",
                notes = notesEditTextModel.editTextContent ?: "",
                createdAt = System.currentTimeMillis() / 1000,
                updatedAt = System.currentTimeMillis() / 1000,
                webAddress = websiteAddressEditTextModel.editTextContent ?: "",
                tagId = selectedFolderId
            )
        )
    }
}