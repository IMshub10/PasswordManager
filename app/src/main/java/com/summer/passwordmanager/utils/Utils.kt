package com.summer.passwordmanager.utils

import android.app.Activity
import android.util.DisplayMetrics

object Utils {
    fun getScreenWidthIntDp(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return (displayMetrics.widthPixels)
    }
}