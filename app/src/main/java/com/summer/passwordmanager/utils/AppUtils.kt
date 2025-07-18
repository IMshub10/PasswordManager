package com.summer.passwordmanager.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.github.shamil.Xid
import com.google.gson.Gson
import com.summer.passwordmanager.beans.FileBean
import com.summer.passwordmanager.utils.extensions.fromJson
import java.util.Calendar
import java.util.Random
import java.util.TimeZone
import kotlin.streams.asSequence


object AppUtils {
    const val PIN_LENGTH = 4

    //region Functions
    fun generateXid() = Xid.get().toHexString()!!

    fun getCurrentTimeSecs() =
        Calendar.getInstance(TimeZone.getTimeZone("GMT")).timeInMillis / 1000

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

    fun copyText(context: Context?, textToBeCopied: String) {
        (context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)?.setPrimaryClip(
            ClipData.newPlainText(
                "Copy", textToBeCopied
            )
        )
    }

    fun FileBean.toJSON(): String = Gson().toJson(this)

    fun String.toFileBean() = Gson().fromJson<FileBean>(this)!!

    fun getAppName(context: Context): String {
        val applicationInfo = context.applicationInfo
        val stringId = applicationInfo.labelRes
        return if (stringId == 0) {
            applicationInfo.nonLocalizedLabel.toString()
        } else {
            context.getString(stringId)
        }
    }

    fun getFileName(resolver: ContentResolver, uri: Uri): String? {
        val returnCursor: Cursor? = resolver.query(uri, null, null, null, null)
        val nameIndex: Int =
            returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: return null
        returnCursor.moveToFirst()
        val name: String? = returnCursor.getString(nameIndex)
        returnCursor.close()
        return name
    }
}