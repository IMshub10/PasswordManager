package com.summer.passwordmanager.utils

fun System.currentTimeSecs() :Long{
    return System.currentTimeMillis() / 1000
}


fun List<Int>?.getIntArray(): String? {
    if (this == null) return null
    return if (this.isEmpty()) {
        "[]"
    } else {
        var resultString = "["
        this.forEach {
            resultString += it.toString()
            resultString += ","
        }
        resultString = resultString.substring(0, resultString.length - 1)
        resultString += "]"
        resultString
    }
}