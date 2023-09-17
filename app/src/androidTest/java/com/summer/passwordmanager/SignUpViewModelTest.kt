package com.summer.passwordmanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.summer.passwordmanager.database.preferences.Preference
import com.summer.passwordmanager.repository.UserRepository
import com.summer.passwordmanager.repository.UserRepositoryImpl
import com.summer.passwordmanager.ui.screens.signup.viewmodels.SignUpViewModel
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpViewModelTest {
    private lateinit var viewModel: SignUpViewModel
    private lateinit var repository: UserRepository

    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher)


    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        repository = UserRepositoryImpl(
            sharedPreferences = Preference.createdEncryptedPreference(context)
        )
        viewModel = SignUpViewModel(repository)
    }

    @Test
    fun saveJohnDoe() = testCoroutineScope.runTest {
        val fullName = "John Doe"
        viewModel.fullNameEditTextModel.editTextContent = fullName
        viewModel.save()

        val savedFullName = viewModel.checkUserHasSavedHisName()

        assertTrue(savedFullName)
    }


    @Test
    fun saveNull() = testCoroutineScope.runTest {
        val fullName = null
        viewModel.fullNameEditTextModel.editTextContent = fullName
        viewModel.save()

        val savedFullName = viewModel.checkUserHasSavedHisName()

        assertFalse(savedFullName)
    }

}