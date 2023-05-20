package com.summer.passwordmanager.utils

import android.text.InputFilter

object FilterUtils {

    var alphaNumericWhiteSpaceFilter = InputFilter { source, start, end, dest, _, _ ->
        for (i in start until end) {
            if (!Regex("[A-Za-z0-9\\s]*").matches(source.toString() + dest.toString())) { // Accept alpha numeric characters
                return@InputFilter ""
            }
        }
        null
    }

    var alphaWhiteSpaceFilter = InputFilter { source, start, end, dest, _, _ ->
        for (i in start until end) {
            if (!Regex("[A-Za-z]*").matches(dest.toString() + source.toString())) { // Accept alpha numeric characters
                return@InputFilter ""
            }
        }
        null
    }

    /**
     * ^(\+\d{1,3}( )?)?((\(\d{1,3}\))|\d{1,3})[- .]?\d{3,4}[- .]?\d{4}$
     */
    var universalPhoneFilter = InputFilter { source, start, end, dest, _, _ ->
        if (source.contains("+")) {
            if (dest.contains("+")) {
                return@InputFilter ""
            }
        }
        for (i in start until end) {
            if (!Regex("[0-9+]*").matches(
                    dest.toString() + source.toString()
                )
            ) { // Accept alpha numeric characters
                return@InputFilter ""
            }
        }
        null
    }
}