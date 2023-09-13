package com.summer.passwordmanager

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.summer.passwordmanager.database.AppDatabase
import com.summer.passwordmanager.database.preferences.Preference
import com.summer.passwordmanager.repository.AppRepository
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.ui.screens.main.viewmodels.PassGeneratorViewModel
import com.summer.passwordmanager.utils.AppUtils
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PassGeneratorViewModelTest {

    private lateinit var viewModel: PassGeneratorViewModel
    private lateinit var repository: Repository

    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher)

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repository = AppRepository(
            Room.databaseBuilder(context, AppDatabase::class.java, "pass_generator_db")
                .createFromAsset("db/init.db")
                .build().appDao(),
            Preference.createdEncryptedPreference(context)
        )
        viewModel = PassGeneratorViewModel(repository)
    }

    @Test
    fun lowerAndUpperAlphasPassword() = testCoroutineScope.runTest {
        val password = "TestPassword"
        val currentTime = AppUtils.getCurrentTimeSecs()
        viewModel.passGeneratorModel.apply {
            hasLowerAlphas = true
            hasUpperAlphas = true
            hasNumbers = false
            hasSpecialCharacters = false
        }
        val passHistoryModel = viewModel.buildPassHistoryModel(password).apply {
            createdAtApp = currentTime
            updatedAtApp = currentTime
        }

        viewModel.insertPassHistory(passHistoryModel)
        
        val dbPassHistoryModel = repository.getPassHistoryModel(passHistoryModel.id)
        assert(dbPassHistoryModel?.password == passHistoryModel.password)
        assert(dbPassHistoryModel?.hasLowerAlphas == viewModel.passGeneratorModel.hasLowerAlphas)
        assert(dbPassHistoryModel?.hasUpperAlphas == viewModel.passGeneratorModel.hasUpperAlphas)
        assert(dbPassHistoryModel?.hasNumbers == viewModel.passGeneratorModel.hasNumbers)
        assert(dbPassHistoryModel?.hasSpecialCharacters == viewModel.passGeneratorModel.hasSpecialCharacters)
    }

    @Test
    fun lowerAndUpperAlphasAndNumbersPassword() = testCoroutineScope.runTest {
        val password = "TestPassword123"
        val currentTime = AppUtils.getCurrentTimeSecs()
        viewModel.passGeneratorModel.apply {
            hasLowerAlphas = true
            hasUpperAlphas = true
            hasNumbers = true
            hasSpecialCharacters = false
        }
        val passHistoryModel = viewModel.buildPassHistoryModel(password).apply {
            createdAtApp = currentTime
            updatedAtApp = currentTime
        }

        viewModel.insertPassHistory(passHistoryModel)

        val dbPassHistoryModel = repository.getPassHistoryModel(passHistoryModel.id)
        
        Log.e("Test", dbPassHistoryModel?.password.toString())
        Log.e("Test", passHistoryModel.password)
        assert(dbPassHistoryModel?.password == passHistoryModel.password)
        assert(dbPassHistoryModel?.hasLowerAlphas == viewModel.passGeneratorModel.hasLowerAlphas)
        assert(dbPassHistoryModel?.hasUpperAlphas == viewModel.passGeneratorModel.hasUpperAlphas)
        assert(dbPassHistoryModel?.hasNumbers == viewModel.passGeneratorModel.hasNumbers)
        assert(dbPassHistoryModel?.hasSpecialCharacters == viewModel.passGeneratorModel.hasSpecialCharacters)
    }

    @Test
    fun lowerAlphasPassword() = testCoroutineScope.runTest {
        val password = "test"
        val currentTime = AppUtils.getCurrentTimeSecs()
        viewModel.passGeneratorModel.apply {
            hasLowerAlphas = true
            hasUpperAlphas = false
            hasNumbers = false
            hasSpecialCharacters = false
        }
        val passHistoryModel = viewModel.buildPassHistoryModel(password).apply {
            createdAtApp = currentTime
            updatedAtApp = currentTime
        }

        viewModel.insertPassHistory(passHistoryModel)
        
        val dbPassHistoryModel = repository.getPassHistoryModel(passHistoryModel.id)
        assert(dbPassHistoryModel?.password == passHistoryModel.password)
        assert(dbPassHistoryModel?.hasLowerAlphas == viewModel.passGeneratorModel.hasLowerAlphas)
        assert(dbPassHistoryModel?.hasUpperAlphas == viewModel.passGeneratorModel.hasUpperAlphas)
        assert(dbPassHistoryModel?.hasNumbers == viewModel.passGeneratorModel.hasNumbers)
        assert(dbPassHistoryModel?.hasSpecialCharacters == viewModel.passGeneratorModel.hasSpecialCharacters)
    }
    @Test
    fun specialCharactersPassword() = testCoroutineScope.runTest {
        val password = "&&^**^"
        val currentTime = AppUtils.getCurrentTimeSecs()
        viewModel.passGeneratorModel.apply {
            hasLowerAlphas = false
            hasUpperAlphas = false
            hasNumbers = false
            hasSpecialCharacters = true
        }
        val passHistoryModel = viewModel.buildPassHistoryModel(password).apply {
            createdAtApp = currentTime
            updatedAtApp = currentTime
        }

        viewModel.insertPassHistory(passHistoryModel)

        val dbPassHistoryModel = repository.getPassHistoryModel(passHistoryModel.id)
        assert(dbPassHistoryModel?.password == passHistoryModel.password)
        assert(dbPassHistoryModel?.hasLowerAlphas == viewModel.passGeneratorModel.hasLowerAlphas)
        assert(dbPassHistoryModel?.hasUpperAlphas == viewModel.passGeneratorModel.hasUpperAlphas)
        assert(dbPassHistoryModel?.hasNumbers == viewModel.passGeneratorModel.hasNumbers)
        assert(dbPassHistoryModel?.hasSpecialCharacters == viewModel.passGeneratorModel.hasSpecialCharacters)
    }
}
