package com.summer.passwordmanager.ui.screens.signup.viewmodels

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.repository.Repository

class SetUpPinViewModel(private val repository: Repository) : ViewModel() {

    suspend fun save(checked: Boolean?) {
        repository.setPin(pin = firstPin ?: "")
        repository.setFingerPrint(checked ?: false)
    }

    var firstPin: String? = null
    var secondPin: String? = null
}