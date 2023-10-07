package com.summer.passwordmanager.repository

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.summer.passwordmanager.beans.FileBean
import com.summer.passwordmanager.utils.AESEncryption
import com.summer.passwordmanager.utils.AppUtils.toFileBean
import com.summer.passwordmanager.utils.AppUtils.toJSON
import com.summer.passwordmanager.utils.EncryptionUtils
import com.summer.passwordmanager.utils.EncryptionUtils.toCharArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.Base64

class FileRepositoryImpl : FileRepository {

    override suspend fun exportFile(
        fileBean: FileBean,
        appName: String,
        key: String,
        fileOutputStream: OutputStream
    ) {
        withContext(Dispatchers.IO) {
            try {
                val fileBeanJson = fileBean.toJSON()
                Log.d("TestingJson", fileBeanJson)
                val ivGen = EncryptionUtils.generateRandomBytes(16) //IV

                Log.d("TestingJson", "KeyGen $key")
                Log.d("TestingJson", "IVGen ${Base64.getEncoder().encodeToString(ivGen)}")
                val aesEncryption = AESEncryption(
                    key.toCharArray(), ivGen.toCharArray()
                )
                val encryptedString = aesEncryption.encrypt(fileBeanJson)
                Log.d("TestingJson", "Encrypted $encryptedString")

                fileOutputStream.bufferedWriter().use {
                    it.write(ivGen.toCharArray())
                    it.write("\n")
                    it.write(encryptedString)
                }

            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
                e.printStackTrace()
            } finally {
                fileOutputStream.flush()
                fileOutputStream.close()
            }
        }
    }

    override suspend fun importFile(inputStream: InputStream, key: String): FileBean? {
        var fileBean: FileBean? = null
        var fileStream: BufferedReader? = null
        try {
            fileStream = BufferedReader(InputStreamReader(inputStream))
            withContext(Dispatchers.IO) {
                val iiv = fileStream.readLine()
                Log.d("iv", String(iiv.toByteArray()))
                val decryptedString = AESEncryption(
                    key.toCharArray(), iiv.toCharArray()
                ).decryptToString(
                    StringBuffer().apply {
                        fileStream.forEachLine {
                            append(it)
                        }
                    }.toString()
                )
                Log.d("TestingJson", "Decrypted $decryptedString")
                fileBean = decryptedString.toFileBean()
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        } finally {
            fileStream?.close()
        }
        return fileBean
    }
}