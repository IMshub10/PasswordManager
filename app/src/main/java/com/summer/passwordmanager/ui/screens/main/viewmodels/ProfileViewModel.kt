package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.screens.main.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    val userModel = UserModel()

    init {
        viewModelScope.launch(Dispatchers.Default) {
            val userName = repository.getFullName()
            val userMobile = repository.getMobileNumber()
            withContext(Dispatchers.Main) {
                userModel.apply {
                    name = userName ?: ""
                    mobile = userMobile ?: ""
                    notifyChange()
                }
            }
        }
    }
}