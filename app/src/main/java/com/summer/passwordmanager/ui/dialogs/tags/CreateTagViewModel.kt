package com.summer.passwordmanager.ui.dialogs.tags

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.repository.UserRepository
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import com.summer.passwordmanager.utils.AppUtils

class CreateTagViewModel() : ViewModel() {

    var tagEntity: TagEntity? = null

    val tagNameEditTextModel by lazy {
        TextEditTextModel(fieldType = TextEditTextFieldType.TAG_NAME, isRequired = true)
    }

    val tagDescriptionEditTextModel by lazy {
        TextEditTextModel(fieldType = TextEditTextFieldType.TAG_DESCRIPTION, isRequired = false)
    }

    fun setUpInputModels() {
        tagNameEditTextModel.editTextContent = tagEntity?.name.orEmpty()
        tagDescriptionEditTextModel.editTextContent = tagEntity?.description.orEmpty()
    }

    fun toSaveTagEntity() = tagEntity?.apply {
        name = tagNameEditTextModel.editTextContent.orEmpty()
        description = tagDescriptionEditTextModel.editTextContent
        updatedAtApp = AppUtils.getCurrentTimeSecs()
    }
        ?: TagEntity(
            name = tagNameEditTextModel.editTextContent.orEmpty(),
            description = tagDescriptionEditTextModel.editTextContent
        ).apply {
            createdAtApp = AppUtils.getCurrentTimeSecs()
            updatedAtApp = AppUtils.getCurrentTimeSecs()
        }

}