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

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val fullName = userRepository.getFullName()
            withContext(Dispatchers.Main) {
                fullNameEditTextModel.editTextContent = fullName
                fullNameEditTextModel.notifyChange()
            }
        }
    }

    suspend fun saveUserNameMobile() {
        userRepository.save(
            fullName = fullNameEditTextModel.editTextContent,
        )
    }
}