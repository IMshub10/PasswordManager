package com.summer.passwordmanager.ui.screens.splashscreen.viewmodels

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.repository.Repository

class SplashScreenViewModel(private val repository: Repository) : ViewModel() {

    suspend fun checkIfAccountExists(): Boolean {
        return repository.getFullName() != null && repository.getMobileNumber() != null && repository.getPin() != null
    }

    suspend fun isFingerPrintEnabled(): Boolean {
        return repository.isFingerPrintSet()
    }
}