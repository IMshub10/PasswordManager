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
        if (length < 4) {
            throw RuntimeException("Length < 4")
        }
        val upperAlphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val lowerAlphas = "abcdefghijklmnopqrstuvwxyz"
        val numbers = "0123456789"
        val specialChars = "@#$%^&*"
        val source = upperAlphas + lowerAlphas + numbers + specialChars
        return Random().ints(length.toLong(), 0, source.length)
            .asSequence()
            .map(source::get)
            .joinToString("")
    }
}