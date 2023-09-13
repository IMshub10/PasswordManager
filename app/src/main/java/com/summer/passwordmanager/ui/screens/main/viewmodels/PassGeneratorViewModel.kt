package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.screens.main.models.PassGeneratorModel
import com.summer.passwordmanager.utils.AppUtils

class PassGeneratorViewModel(private val repository: Repository) : ViewModel() {
    val passGeneratorModel = PassGeneratorModel()

    suspend fun insertPassHistory(
        passwordHistoryEntity: PassHistoryEntity,
    ) {
        repository.insertPastHistory(
            passwordHistoryEntity
        )
    }

    fun buildPassHistoryModel(password: String) =
        PassHistoryEntity(
            id = AppUtils.generateXid(),
            password = password,
            hasLowerAlphas = passGeneratorModel.hasLowerAlphas,
            hasUpperAlphas = passGeneratorModel.hasUpperAlphas,
            hasNumbers = passGeneratorModel.hasNumbers,
            hasSpecialCharacters = passGeneratorModel.hasSpecialCharacters,
            createdAtApp = AppUtils.getCurrentTimeSecs(),
            updatedAtApp = AppUtils.getCurrentTimeSecs(),
        )

}