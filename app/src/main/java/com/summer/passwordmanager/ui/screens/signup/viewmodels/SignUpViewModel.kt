package com.summer.passwordmanager.ui.screens.signup.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: Repository) : ViewModel() {
    val fullNameEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.FULL_NAME, isRequired = true)
    val mobileNumberEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.MOBILE_NUMBER, isRequired = true)

    suspend fun save() {
        repository.save(
            fullNameEditTextModel.editTextContent,
            mobileNumberEditTextModel.editTextContent
        )
    }
}