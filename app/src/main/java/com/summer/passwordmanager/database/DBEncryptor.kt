package com.summer.passwordmanager.database

import android.content.SharedPreferences
import android.util.Base64
import com.google.gson.JsonSyntaxException
import com.summer.passwordmanager.database.preferences.Preference.Companion.KEY_DB_PASSCODE
import com.summer.passwordmanager.database.preferences.Preference.Companion.KEY_DB_WRAPPER_DATA
import net.sqlcipher.database.SupportFactory
import com.google.gson.Gson
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.EncryptionUtils.toByteArray
import com.summer.passwordmanager.utils.extensions.fromJson
import com.summer.passwordmanager.utils.extensions.json
import net.sqlcipher.database.SQLiteDatabase
import java.security.AlgorithmParameters
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


object DBEncryptor {

    private const val ALGORITHM = "PBKDF2WithHmacSHA256"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    private fun generateSecretKey(passcode: CharArray, salt: ByteArray): SecretKey {
        // Initialize PBE with password
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(passcode, salt, 65536, 256)
        val tmp: SecretKey = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }

    private fun CharArray.toEncryptWrapper(rawDbKey: ByteArray): EncryptWrapper {
        // Generate a random 8 byte salt
        val salt = ByteArray(8).apply {
            SecureRandom.getInstanceStrong().nextBytes(this)
        }
        val secret: SecretKey = generateSecretKey(this, salt)

        // Now encrypt the database key with PBE
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secret)
        val params: AlgorithmParameters = cipher.parameters
        val iv: ByteArray = params.getParameterSpec(IvParameterSpec::class.java).iv
        val ciphertext: ByteArray = cipher.doFinal(rawDbKey)

        // Return the IV and CipherText which can be stored to disk
        return EncryptWrapper(
            Base64.encodeToString(iv, Base64.DEFAULT),
            Base64.encodeToString(ciphertext, Base64.DEFAULT),
            Base64.encodeToString(salt, Base64.DEFAULT)
        )
    }


    private fun getRawByteKey(passcode: CharArray, wrapper: EncryptWrapper): ByteArray {
        val aesWrappedKey = Base64.decode(wrapper.key, Base64.DEFAULT)
        val iv = Base64.decode(wrapper.iv, Base64.DEFAULT)
        val salt = Base64.decode(wrapper.salt, Base64.DEFAULT)
        val secret: SecretKey = generateSecretKey(passcode, salt)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secret, IvParameterSpec(iv))
        return cipher.doFinal(aesWrappedKey)
    }

    private fun getWrapper(preference: SharedPreferences): EncryptWrapper? {
        val data = preference.getString(KEY_DB_WRAPPER_DATA, "")!!
        if (data.isEmpty()) {
            return null
        }
        return try {
            Gson().fromJson<EncryptWrapper>(data)
        } catch (ex: JsonSyntaxException) {
            null
        }
    }

    private fun ByteArray.toHex(): CharArray {
        val result = StringBuilder()
        forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS[firstIndex])
            result.append(HEX_CHARS[secondIndex])
        }
        return result.toString().toCharArray()
    }


    private fun generateNewByteKey(): ByteArray =
        ByteArray(32).apply {
            val s = StringBuilder()
            while (s.length < 32) {
                s.append(AppUtils.generateXid())
            }
            s.substring(0, 32).toString().toCharArray().toByteArray()
        }

    operator fun invoke(preference: SharedPreferences): SupportFactory {
        var password = preference.getString(KEY_DB_PASSCODE, "")!!
        val encryptWrapper = getWrapper(preference = preference)
        // creating a password if empty
        if (password.isEmpty()) {
            val newKey = AppUtils.generateXid()
            preference.edit().putString(KEY_DB_PASSCODE, newKey).apply()
            password = newKey
        }


        val rawByteKey = if (encryptWrapper == null) {
            val rawByte = generateNewByteKey()
            val newWrapper = password.toCharArray().toEncryptWrapper(rawByte)
            preference.edit().putString(KEY_DB_WRAPPER_DATA, newWrapper.json()).apply()
            getRawByteKey(password.toCharArray(), newWrapper)
        } else {
            getRawByteKey(password.toCharArray(), encryptWrapper)
        }

        val dbCharKey = rawByteKey.toHex()

        return SupportFactory(SQLiteDatabase.getBytes(dbCharKey))
    }

}
