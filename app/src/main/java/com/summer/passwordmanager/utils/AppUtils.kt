package com.summer.passwordmanager.utils

import com.github.shamil.Xid
import java.util.Random
import kotlin.streams.asSequence

object AppUtils {
    //region Values
    const val KEY_FULL_NAME = "full_name"
    const val KEY_MOBILE_NUMBER = "mobile_number"
    //endregion


    //region Functions
    fun generateXid(): String {
        return Xid.get().toHexString()
    }

    /**
     * Generate Random String
     * returns String of specified length
     */
    fun getRandomString(
        length: Int,
        hasUpperAlphas: Boolean = true,
        hasLowerAlphas: Boolean = true,
        hasNumbers: Boolean = true,
        hasSpecialCharacters: Boolean = true
    ): String {
        val upperAlphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val lowerAlphas = "abcdefghijklmnopqrstuvwxyz"
        val numbers = "0123456789"
        val specialChars = "@#$%^&*"
        if (length < 4) {
            throw RuntimeException("Length < 4")
        }
        val stringBuilder = StringBuilder()
        if (hasUpperAlphas) {
            stringBuilder.append(upperAlphas)
        }
        if (hasLowerAlphas) {
            stringBuilder.append(lowerAlphas)
        }
        if (hasNumbers) {
            stringBuilder.append(numbers)
        }
        if (hasSpecialCharacters) {
            stringBuilder.append(specialChars)
        }
        return Random().ints(length.toLong(), 0, stringBuilder.toString().length)
            .asSequence()
            .map(stringBuilder.toString()::get)
            .joinToString("")
    }
    //endregion
}