package com.summer.passwordmanager.ui.screens.pin.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PinViewModel(private val repository: Repository) : ViewModel() {

    var pin: String? = "12345"
    val isFingerPrintEnabled = ObservableField(false)

    fun init() {
        viewModelScope.launch(Dispatchers.Default) {
            pin = repository.getPin()
            val isFingerprintSet = repository.isFingerPrintSet()
            withContext(Dispatchers.Main) {
                isFingerPrintEnabled.set(isFingerprintSet)
                isFingerPrintEnabled.notifyChange()
            }
        }
    }
}