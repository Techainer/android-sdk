package com.liveness.sdk.core.utils

import android.util.Base64
import com.liveness.sdk.core.a
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher


/**
 * @author Anass AIT BEN EL ARBI
 *
 *  * AES/CBC/NoPadding (128)
 *  * AES/CBC/PKCS5Padding (128)
 *  * AES/ECB/NoPadding (128)
 *  * AES/ECB/PKCS5Padding (128)
 *  * RSA/ECB/PKCS1Padding (1024, 2048)
 *  * RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)
 *  * RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)
 *
 *
 *
 * for more details @see [Java Ciphers](https://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html)
 */
class RSACryptDataBio {
    private var privateKey: PrivateKey? = null
    private var publicKey: PublicKey? = null

    fun initFromStrings() {
        try {
            val keySpecPublic = X509EncodedKeySpec(decode(PUBLIC_KEY_STRING))
            val keySpecPrivate = PKCS8EncodedKeySpec(decode(PRIVATE_KEY_STRING))
            val keyFactory = KeyFactory.getInstance("RSA")
            publicKey = keyFactory.generatePublic(keySpecPublic);
            privateKey = keyFactory.generatePrivate(keySpecPrivate)
        } catch (ignored: Exception) {
        }
    }

    @Throws(java.lang.Exception::class)
    fun encrypt(message: String): String {
        initFromStrings()
        val messageToBytes = message.toByteArray()
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encryptedBytes = cipher.doFinal(messageToBytes)
        return encode(encryptedBytes)
    }

    private fun encode(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun decrypt(encryptedMessage: String): String {
        initFromStrings()
        val encryptedBytes = decode(encryptedMessage)
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedMessage = cipher.doFinal(encryptedBytes)
        return String(decryptedMessage, charset("UTF8"))
    }

    companion object {
//        private const val PRIVATE_KEY_STRING =
//            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJhBgzcXBm5A0srvFFu4FsBy+LLW+X0sH/9RvP40VIGOCusY0/CqA65YXWqyQE5jQCegBmnAeVYSvK+3PU4Y1fmr1uiquE6sZB5sl96T0ka+PKzPf4oKoAi6nwLUSenj5xTFjLsFGiuMXrCpMCPImf9JBVk89TJV43Xs3DSNKoj1AgMBAAECgYBsDysCgVv2ChnRH4eSZP/4zGCIBR0C4rs+6RM6U4eaf2ZuXqulBfUg2uRKIoKTX8ubk+6ZRZqYJSo3h9SBxgyuUrTehhOqmkMDo/oa9v7aUqAKw/uoaZKHlj+3p4L3EK0ZBpz8jjs/PXJc77Lk9ZKOUY+T0AW2Fz4syMaQOiETzQJBANF5q1lntAXN2TUWkzgir+H66HyyOpMu4meaSiktU8HWmKHa0tSB/v7LTfctnMjAbrcXywmb4ddixOgJLlAjEncCQQC6Enf3gfhEEgZTEz7WG9ev/M6hym4C+FhYKbDwk+PVLMVR7sBAtfPkiHVTVAqC082E1buZMzSKWHKAQzFL7o7zAkBye0VLOmLnnSWtXuYcktB+92qh46IhmEkCCA+py2zwDgEiy/3XSCh9Rc0ZXqNGD+0yQV2kpb3awc8NZR8bit9nAkBo4TgVnoCdfbtq4BIvBQqR++FMeJmBuxGwv+8n63QkGFQwVm6vCuAqFHBtQ5WZIGFbWk2fkKkwwaHogfcrYY/ZAkEAm5ibtJx/jZdPEF9VknswFTDJl9xjIfbwtUb6GDMc0KH7v+QTBW4GsHwt/gL+kGvLOLcEdLL5rau3IC7EQT0ZYg=="

        private val PRIVATE_KEY_STRING = a().getAAF("com.liveness.sdk.core")
        private const val PUBLIC_KEY_STRING =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYQYM3FwZuQNLK7xRbuBbAcviy1vl9LB//Ubz+NFSBjgrrGNPwqgOuWF1qskBOY0AnoAZpwHlWEryvtz1OGNX5q9boqrhOrGQebJfek9JGvjysz3+KCqAIup8C1Enp4+cUxYy7BRorjF6wqTAjyJn/SQVZPPUyVeN17Nw0jSqI9QIDAQAB"

        private fun encode(data: ByteArray): String {
            return Base64.encodeToString(data, Base64.DEFAULT)
        }

        private fun decode(data: String): ByteArray {
            return Base64.decode(data, Base64.DEFAULT)
        }
    }
}