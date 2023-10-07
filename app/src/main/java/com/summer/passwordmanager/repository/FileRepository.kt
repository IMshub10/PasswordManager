package com.summer.passwordmanager.repository

import com.summer.passwordmanager.beans.FileBean
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

interface FileRepository {

    //region File methods
    suspend fun exportFile(
        fileBean: FileBean,
        appName: String,
        key: String,
        fileOutputStream: OutputStream
    )

    suspend fun importFile(inputStream: InputStream, key: String): FileBean?
    //endregion
}