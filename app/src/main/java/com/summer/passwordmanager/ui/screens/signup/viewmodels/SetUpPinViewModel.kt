package com.summer.passwordmanager.ui.screens.signup.viewmodels

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.repository.UserRepository

class SetUpPinViewModel(private val userRepository: UserRepository) : ViewModel() {

    suspend fun save(checked: Boolean?) {
        userRepository.setPin(pin = firstPin.orEmpty())
        userRepository.setFingerPrint(checked ?: false)
    }

    var firstPin: String? = null
    var secondPin: String? = null
}