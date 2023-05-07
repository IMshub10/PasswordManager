package com.summer.passwordmanager

import com.summer.passwordmanager.utils.Utils.getRandomString
import junit.framework.TestCase.assertEquals
import org.junit.Test

class PasswordGeneratorTest {
    @Test
    fun testDefaultConfiguration() {
        val password = getRandomString(length = 10)
        assertEquals(10, password.length)
    }

    @Test
    fun testOnlyUpperAlphas() {
        val password = getRandomString(
            length = 10,
            hasUpperAlphas = true,
            hasLowerAlphas = false,
            hasNumbers = false,
            hasSpecialCharacters = false
        )
        assertEquals(10, password.length)
        assertEquals(true, password.all { it.isUpperCase() })
    }

    @Test
    fun testOnlyLowerAlphas() {
        val password = getRandomString(
            length = 10,
            hasUpperAlphas = false,
            hasLowerAlphas = true,
            hasNumbers = false,
            hasSpecialCharacters = false
        )
        assertEquals(10, password.length)
        assertEquals(true, password.all { it.isLowerCase() })
    }

    @Test
    fun testOnlyNumbers() {
        val password = getRandomString(
            length = 10,
            hasUpperAlphas = false,
            hasLowerAlphas = false,
            hasNumbers = true,
            hasSpecialCharacters = false
        )
        assertEquals(10, password.length)
        assertEquals(true, password.all { it.isDigit() })
    }

    @Test
    fun testOnlySpecialCharacters() {
        val password = getRandomString(
            length = 10,
            hasUpperAlphas = false,
            hasLowerAlphas = false,
            hasNumbers = false,
            hasSpecialCharacters = true
        )
        assertEquals(10, password.length)
        assertEquals(true, password.all { it in "@#$%^&*" })
    }

}
