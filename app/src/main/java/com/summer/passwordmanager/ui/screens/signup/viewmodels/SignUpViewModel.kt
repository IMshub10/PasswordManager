package com.summer.passwordmanager.ui.screens.signup.viewmodels

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel

class SignUpViewModel(private val repository: Repository) : ViewModel() {
    val fullNameEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.FULL_NAME, isRequired = true)
    val phoneEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.MOBILE_NUMBER, isRequired = true)
}