package com.summer.passwordmanager.ui.dialogs

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import com.summer.passwordmanager.utils.time.TimeProvider

class CreateTagViewModel(private val timeProvider: TimeProvider) : ViewModel() {

    var tagEntity: TagEntity? = null

    val tagNameEditTextModel by lazy {
        TextEditTextModel(fieldType = TextEditTextFieldType.TAG_NAME, isRequired = true)
    }

    val tagDescriptionEditTextModel by lazy {
        TextEditTextModel(fieldType = TextEditTextFieldType.TAG_DESCRIPTION, isRequired = false)
    }

    fun setUpInputModels() {
        tagNameEditTextModel.editTextContent = tagEntity?.name ?: ""
        tagDescriptionEditTextModel.editTextContent = tagEntity?.description ?: ""
    }

    fun toSaveTagEntity() = tagEntity?.apply {
        name = tagNameEditTextModel.editTextContent ?: ""
        description = tagDescriptionEditTextModel.editTextContent
        updatedAtApp = timeProvider.getCurrentTimeSecs()
    }
        ?: TagEntity(
            name = tagNameEditTextModel.editTextContent ?: "",
            description = tagDescriptionEditTextModel.editTextContent
        ).apply {
            createdAtApp = timeProvider.getCurrentTimeSecs()
            updatedAtApp = timeProvider.getCurrentTimeSecs()
        }

}