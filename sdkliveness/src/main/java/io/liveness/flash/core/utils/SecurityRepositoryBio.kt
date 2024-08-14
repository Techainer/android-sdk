package io.liveness.flash.core.utils

import android.content.Context
import android.util.Base64

object SecurityRepositoryBio {
    fun getProperty(key: String, context: Context): EncryptedInfoBio {
        val sharePreferenceUtils = AppPreferenceUtilsBio(context)
        val info = EncryptedInfoBio()

        val dataStr = sharePreferenceUtils.getValueString(key, null)
        dataStr?.let {
            info.data = Base64.decode(dataStr, Base64.DEFAULT)
        }

        val iv = sharePreferenceUtils.getValueString("${key}_iv", null)
        iv?.let {
            info.iv = Base64.decode(iv, Base64.DEFAULT)
        }
        return info
    }

    fun setProperty(key: String, encryptedValue: ByteArray?, iv: ByteArray?, context: Context) {
        if (encryptedValue != null && iv != null) {
            val sharePreferenceUtils = AppPreferenceUtilsBio(context)
            val ivString = Base64.encodeToString(iv, Base64.DEFAULT)
            val encryptedString = Base64.encodeToString(encryptedValue, Base64.DEFAULT)

            sharePreferenceUtils.setValueString(key, encryptedString)

            sharePreferenceUtils.setValueString("${key}_iv", ivString)
        }
    }
    fun setProperty(key: String, encryptedValue: String?, iv: ByteArray?, context: Context) {
        if (encryptedValue != null && iv != null) {
            val sharePreferenceUtils = AppPreferenceUtilsBio(context)
            val ivString = Base64.encodeToString(iv, Base64.DEFAULT)
//            val encryptedString = Base64.encodeToString(encryptedValue, Base64.DEFAULT)

            sharePreferenceUtils.setValueString(key, encryptedValue)

            sharePreferenceUtils.setValueString("${key}_iv", ivString)
        }
    }
}