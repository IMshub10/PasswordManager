package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.repository.LocalRepository
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import com.summer.passwordmanager.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateVaultViewModel(private val localRepository: LocalRepository) : ViewModel() {

    var selectedTagId: String? = null

    private var vaultEntity: VaultEntity? = null

    val websiteNameEditTextModel by lazy {
        TextEditTextModel(fieldType = TextEditTextFieldType.VAULT_NAME, isRequired = true)
    }
    val websiteAddressEditTextModel by lazy {
        TextEditTextModel(fieldType = TextEditTextFieldType.WEBSITE_ADDRESS, isRequired = false)
    }
    val userNameMobileEditTextModel by lazy {
        TextEditTextModel(fieldType = TextEditTextFieldType.USERNAME_MOBILE, isRequired = true)
    }
    val passwordEditTextModel by lazy {
        TextEditTextModel(fieldType = TextEditTextFieldType.PASSWORD, isRequired = true)
    }
    val notesEditTextModel by lazy {
        TextEditTextModel(fieldType = TextEditTextFieldType.NOTES, isRequired = false)
    }

    val tagsLive: LiveData<List<TagEntity>?>
        get() =
            localRepository.getAllTagsLive()

    private fun resetUiModels() {
        selectedTagId = vaultEntity?.tagId
        websiteNameEditTextModel.setModel(vaultEntity?.entityName ?: "")
        websiteAddressEditTextModel.setModel(vaultEntity?.webAddress ?: "")
        userNameMobileEditTextModel.setModel(vaultEntity?.usernameMobileCardNumber ?: "")
        passwordEditTextModel.setModel(vaultEntity?.password ?: "")
        notesEditTextModel.setModel(vaultEntity?.notes ?: "")
    }

    fun setUpVaultEntity(vaultId: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            vaultEntity = vaultId?.let { localRepository.getVaultById(vaultId) }
            withContext(Dispatchers.Main) {
                resetUiModels()
            }
        }
    }

    suspend fun save() {
        vaultEntity?.apply {
            entityName = websiteNameEditTextModel.editTextContent ?: ""
            usernameMobileCardNumber = userNameMobileEditTextModel.editTextContent ?: ""
            password = passwordEditTextModel.editTextContent ?: ""
            notes = notesEditTextModel.editTextContent ?: ""
            webAddress = websiteAddressEditTextModel.editTextContent ?: ""
            tagId = selectedTagId
            updatedAtApp = AppUtils.getCurrentTimeSecs()
        }
        localRepository.insertReplaceVaultEntity(
            vaultEntity ?: VaultEntity(
                id = AppUtils.generateXid(),
                entityName = websiteNameEditTextModel.editTextContent ?: "",
                usernameMobileCardNumber = userNameMobileEditTextModel.editTextContent ?: "",
                password = passwordEditTextModel.editTextContent ?: "",
                notes = notesEditTextModel.editTextContent ?: "",
                createdAtApp = AppUtils.getCurrentTimeSecs(),
                updatedAtApp = AppUtils.getCurrentTimeSecs(),
                webAddress = websiteAddressEditTextModel.editTextContent ?: "",
                tagId = selectedTagId
            )
        )
    }

    suspend fun insertTagEntity(tagEntity: TagEntity) =
        localRepository.insertReplaceTagEntity(tagEntity)

}