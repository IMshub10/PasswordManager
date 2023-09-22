package com.summer.passwordmanager.ui.screens.pin.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinViewModel(private val userRepository: UserRepository) : ViewModel() {

    var pin: String? = "12345"

    private val _isFingerPrintEnabled = MutableStateFlow(false)
    val isFingerPrintEnabled = _isFingerPrintEnabled.asStateFlow()

    val isFingerPrintEnable = ObservableField(false)

    fun init() {
        viewModelScope.launch(Dispatchers.Default) {
            pin = userRepository.getPin()
            val isFingerprintSet = userRepository.isFingerPrintSet()
            withContext(Dispatchers.Main) {
                _isFingerPrintEnabled.value = isFingerprintSet
                isFingerPrintEnable.set(isFingerprintSet)
            }
        }
    }
}