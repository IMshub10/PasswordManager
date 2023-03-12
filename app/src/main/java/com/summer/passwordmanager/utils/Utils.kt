package com.summer.passwordmanager.utils

import android.app.Activity
import android.util.DisplayMetrics
import java.util.*
import kotlin.streams.asSequence

object Utils {
    fun getScreenWidthIntDp(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return (displayMetrics.widthPixels)
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
}