package com.summer.passwordmanager.ui.dialogs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.repository.UserRepository
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val fullNameEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.FULL_NAME, isRequired = true)

    val mobileNumberEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.MOBILE_NUMBER, isRequired = false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val fullName = userRepository.getFullName()
            val mobileNumber = userRepository.getMobileNumber()
            withContext(Dispatchers.Main) {
                fullNameEditTextModel.editTextContent = fullName
                mobileNumberEditTextModel.editTextContent = mobileNumber
                fullNameEditTextModel.notifyChange()
                mobileNumberEditTextModel.notifyChange()
            }
        }
    }

    suspend fun saveUserNameMobile() {
        userRepository.save(
            fullName = fullNameEditTextModel.editTextContent,
            mobileNumber = mobileNumberEditTextModel.editTextContent
        )
    }
}