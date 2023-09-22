package com.summer.passwordmanager

import com.summer.passwordmanager.repository.UserRepository
import com.summer.passwordmanager.ui.screens.splashscreen.viewmodels.SplashScreenViewModel
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashScreenViewModelTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var viewModel: SplashScreenViewModel

    private val testCoroutineDispatcher = StandardTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = SplashScreenViewModel(userRepository)
    }

    @Test
    fun `checkIfAccountExists returns true when all data is available`() =
        testCoroutineScope.runTest {
            Mockito.`when`(userRepository.getFullName()).thenReturn("John Doe")
            Mockito.`when`(userRepository.getPin()).thenReturn("1234")
            assertTrue(viewModel.checkIfAccountExists())
        }

    @Test
    fun `checkIfAccountExists returns false when any data is missing`() =
        testCoroutineScope.runTest {
            Mockito.`when`(userRepository.getFullName()).thenReturn(null)
            assertFalse(viewModel.checkIfAccountExists())
        }

    @Test
    fun `checkIfAccountExists returns false when any data is missing 2`() =
        testCoroutineScope.runTest {
            Mockito.`when`(userRepository.getFullName()).thenReturn("Shubham Meher")
            Mockito.`when`(userRepository.getPin()).thenReturn(null)
            assertFalse(viewModel.checkIfAccountExists())
        }

}