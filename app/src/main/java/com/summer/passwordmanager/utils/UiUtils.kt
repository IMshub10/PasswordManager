package com.summer.passwordmanager.utils

import android.app.Activity
import android.util.DisplayMetrics
import com.github.shamil.Xid
import java.util.*
import kotlin.streams.asSequence

object UiUtils {

    fun getScreenWidthIntDp(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return (displayMetrics.widthPixels)
    }
}