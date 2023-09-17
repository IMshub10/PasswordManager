package com.summer.passwordmanager.ui.screens.signup.viewmodels

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.repository.UserRepository
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel

class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {
    val fullNameEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.FULL_NAME, isRequired = true)

    suspend fun save() {
        userRepository.save(
            fullName = fullNameEditTextModel.editTextContent,
            mobileNumber = null//mobileNumberEditTextModel.editTextContent
        )
    }

    suspend fun checkUserHasSavedHisName() =
        userRepository.getFullName() != null //&& repository.getMobileNumber() != null

}