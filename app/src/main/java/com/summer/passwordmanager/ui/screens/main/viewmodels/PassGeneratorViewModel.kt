package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.screens.main.models.PassGeneratorModel
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.time.TimeProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PassGeneratorViewModel(private val repository: Repository,private val timeProvider: TimeProvider) : ViewModel() {
    val passGeneratorModel = PassGeneratorModel()

    fun insertPassHistory(
        password: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertPastHistory(
                PassHistoryEntity(
                    id = AppUtils.generateXid(),
                    password = password,
                    hasLowerAlphas = passGeneratorModel.hasLowerAlphas,
                    hasUpperAlphas = passGeneratorModel.hasUpperAlphas,
                    hasNumbers = passGeneratorModel.hasNumbers,
                    hasSpecialCharacters = passGeneratorModel.hasSpecialCharacters,
                    createdAtApp = timeProvider.getCurrentTimeSecs(),
                    updatedAtApp = timeProvider.getCurrentTimeSecs(),
                )
            )
        }
    }
}