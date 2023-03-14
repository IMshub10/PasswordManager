package com.summer.passwordmanager.base

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.summer.passwordmanager.ui.screens.main.viewmodels.PassGeneratorViewModel

class AppViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PassGeneratorViewModel::class.java) -> {
                PassGeneratorViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }

}