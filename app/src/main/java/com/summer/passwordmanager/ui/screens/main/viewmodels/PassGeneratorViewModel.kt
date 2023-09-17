package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.database.entities.PassHistoryEntity
import com.summer.passwordmanager.repository.LocalRepository
import com.summer.passwordmanager.ui.screens.main.models.PassGeneratorModel
import com.summer.passwordmanager.utils.AppUtils

class PassGeneratorViewModel(private val localRepository: LocalRepository) : ViewModel() {
    val passGeneratorModel = PassGeneratorModel()

    suspend fun insertPassHistory(
        passwordHistoryEntity: PassHistoryEntity,
    ) = localRepository.insertPastHistory(
        passwordHistoryEntity
    )

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