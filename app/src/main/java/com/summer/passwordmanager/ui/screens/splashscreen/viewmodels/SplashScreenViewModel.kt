package com.summer.passwordmanager.ui.screens.splashscreen.viewmodels

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.repository.UserRepository

class SplashScreenViewModel(private val userRepository: UserRepository) : ViewModel() {

    suspend fun checkIfAccountExists() =
        userRepository.getFullName() != null /*&& repository.getMobileNumber() != null*/ && userRepository.getPin() != null
}