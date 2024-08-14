@file:Suppress("DEPRECATION")

package com.liveness.sdk.core.utils

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.*
import javax.crypto.*
import android.security.KeyPairGeneratorSpec
import android.content.Context
import java.lang.Exception
import java.math.BigInteger
import java.util.*
import javax.security.auth.x500.X500Principal


/**
 * Created by Thuytv2 on 1/10/2019.
 */
class EnCryptDataBio {
    var iv: ByteArray? = null
    var cipher: Cipher? = null

    @Suppress("DEPRECATION")
    @Throws(NoSuchAlgorithmException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    fun getSecretKey(alias: String, context: Context): SecretKey {

        val keyGenerator: KeyGenerator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    alias,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
        } else {
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            end.add(Calendar.YEAR, 30)
            keyGenerator = KeyGenerator
                .getInstance(ANDROID_KEY_STORE)
            val spec = KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(X500Principal("VN=$alias"))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build()
            keyGenerator.init(spec)
        }

        return keyGenerator.generateKey()
    }

    companion object {
        private val ANDROID_KEY_STORE = "AndroidKeyStore"
        private val TRANSFORMATION = "AES/GCM/NoPadding"
    }

    private var encryption: ByteArray? = null

    fun initCipher(context: Context, keyAlias: String): Cipher? {
        try {
            cipher = Cipher.getInstance(TRANSFORMATION)
            cipher?.init(Cipher.ENCRYPT_MODE, getSecretKey(keyAlias, context))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return cipher
    }

    fun encryptText(context: Context?, keyAlias: String, textToEncrypt: String): ByteArray? {
        try {
            context?.apply {
                cipher = Cipher.getInstance(TRANSFORMATION)
                cipher?.init(Cipher.ENCRYPT_MODE, getSecretKey(keyAlias, context)) //AppConfig.KEY_ALIAS

                iv = cipher?.iv

                encryption = cipher?.doFinal(textToEncrypt.toByteArray(charset("UTF-8")))
                SecurityRepositoryBio.setProperty(keyAlias, encryption, iv, context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this.encryption
    }

    fun setEncryptValue(context: Context?, keyAlias: String, textToEncrypt: String) {
        try {
            context?.apply {
                cipher = Cipher.getInstance(TRANSFORMATION)
                cipher?.init(Cipher.ENCRYPT_MODE, getSecretKey(keyAlias, context)) //AppConfig.KEY_ALIAS

                iv = cipher?.iv

//                encryption = cipher?.doFinal(textToEncrypt.toByteArray(charset("UTF-8")))
                SecurityRepositoryBio.setProperty(keyAlias, textToEncrypt, iv, context)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}// Do some things
