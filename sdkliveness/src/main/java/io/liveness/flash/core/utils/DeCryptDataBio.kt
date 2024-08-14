package io.liveness.flash.core.utils

import android.content.Context

import java.io.IOException
import java.lang.Exception
import java.nio.charset.Charset
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec

/**
 * Created by Thuytv2 on 1/10/2019.
 */
class DeCryptDataBio {
    lateinit var keyStore: KeyStore

    init {
        initKeyStore()
    }

    private fun initKeyStore() {
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            keyStore.load(null)
        } catch (e: KeyStoreException) {
           e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    companion object {
        private val ANDROID_KEY_STORE = "AndroidKeyStore"
        private val TRANSFORMATION = "AES/GCM/NoPadding"
    }

    fun decryptData(context: Context, key: String): String {
        try {
            val info = SecurityRepositoryBio.getProperty(key, context)
            val encryptedData = info.data
            val encryptionIv = info.iv
            if (encryptedData != null && encryptionIv != null) {
                val cipher = Cipher.getInstance(TRANSFORMATION)
                val spec = GCMParameterSpec(128, encryptionIv)
                cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key), spec)

                return String(cipher.doFinal(encryptedData), Charset.defaultCharset())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    @Throws(NoSuchAlgorithmException::class, UnrecoverableEntryException::class, KeyStoreException::class)
    private fun getSecretKey(alias: String): SecretKey {
        return (keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }
}
