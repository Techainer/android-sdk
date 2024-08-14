package io.liveness.flash.core.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Thuytv8 on 08/04/2019.
 */
internal class AppPreferenceUtilsBio(context: Context?) {

    private var IShare: SharedPreferences? = null

    init {
        if (context != null)
            IShare = context.getSharedPreferences(
                context.applicationInfo.packageName,
                Context.MODE_PRIVATE
            )
    }

    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////// Remove Share Preferences
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    fun PreferenceUtilRemove(context: Context?) {
        if (context != null)
            IShare = context.getSharedPreferences(
                context.applicationInfo.packageName,
                Context.MODE_PRIVATE
            )
        IShare?.edit()?.clear()?.apply()
    }

    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////// Get Set By Other Key
    /////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    fun setValueLong(key: String, `val`: Long) {
        IShare?.edit()?.putLong(key, `val`)?.apply()
    }

    fun setValueString(key: String?, `val`: String?) {
        IShare?.edit()?.putString(key, `val`)?.apply()
    }

    fun setValueBoolean(key: String, `val`: Boolean) {
        IShare?.edit()?.putBoolean(key, `val`)?.apply()
    }

    fun setValueInteger(key: String, `val`: Int) {
        IShare?.edit()?.putInt(key, `val`)?.apply()
    }

    fun getValueInteger(key: String): Int? {
        return IShare?.getInt(key, -1)
    }

    fun getValueLong(key: String): Long? {
        return IShare?.getLong(key, -1)
    }

    fun getValueBoolean(key: String): Boolean {
        return IShare?.getBoolean(key, false) ?: false
    }

    fun getValueString(key: String?): String? {
        return IShare?.getString(key, null)
    }

    fun getValueString(key: String?, value: String?): String? {
        return IShare?.getString(key, value)
    }

    fun removeValue(key: String) {
        IShare?.edit()?.remove(key)?.apply()
    }

    fun removeAllValue() {
        removeValue(KEY_DEVICE_ID)
        removeValue(KEY_TOTP_SECRET)
        removeValue(KEY_REGISTER_FACE)
        removeValue(KEY_SIGNATURE)
    }

    fun setTOTPSecret(context: Context, strKey: String) {
//        val secret = KeyStoreUtils.getInstance(context)?.encryptData(strKey)
//        val enCryptData = EnCryptData()
//        enCryptData.encryptText(context, KEY_TOTP_SECRET, strKey)
        KeyStoreUtilsBio.getInstance(context)?.encryptData(strKey)
//        setValueString(KEY_TOTP_SECRET, secret)
    }

    fun getTOTPSecret(context: Context): String {
        val secret = getValueString(KEY_TOTP_SECRET)
        if (secret.isNullOrEmpty()) {
            return ""
//            return "ABCDEFGHIJKLMNOP"
        }
        return KeyStoreUtilsBio.getInstance(context)?.decryptData(secret) ?: ""
//        val deCryptData = DeCryptData()
//        return deCryptData.decryptData(context, KEY_TOTP_SECRET)
    }

    fun setDeviceId(strKey: String?) {
        strKey?.let {
            val enCryptData = EnCryptDataBio()
//            enCryptData.encryptText(context, KEY_DEVICE_ID, strKey)
            setValueString(KEY_DEVICE_ID, strKey)
        }

    }

    fun getDeviceId(): String? {
//        val deCryptData = DeCryptData()
//        return deCryptData.decryptData(context, KEY_DEVICE_ID)
        var deviceId = getValueString(KEY_DEVICE_ID)
//        if (deviceId.isNullOrEmpty()) {
//            deviceId = UUID.randomUUID().toString()
//        }
        return deviceId
    }

    fun isRegisterFace(): Boolean {
        return getValueBoolean(KEY_REGISTER_FACE)
    }

    fun setRegisterFace(isRegister: Boolean) {
        return setValueBoolean(KEY_REGISTER_FACE, isRegister)
    }

    fun isFirstVideo(): Boolean {
        return getValueBoolean(KEY_FIRST_VIDEO)
    }

    fun setFirstVideo(isRegister: Boolean) {
        return setValueBoolean(KEY_FIRST_VIDEO, isRegister)
    }

    companion object {
        const val APP_SHARE_PREFERENCE_NAME = "mSharePreferenceName"

        //        const val KEY_DEVICE_ID = "mDeviceId"
        const val KEY_DEVICE_ID = "KEY_DEVICE_ID"

        //        const val KEY_TOTP_SECRET = "mTOTPSecret"
        const val KEY_TOTP_SECRET = "KEY_TOTP_SECRET"
        const val KEY_REGISTER_FACE = "KEY_REGISTER_FACE"
        const val KEY_SIGNATURE = "mSignature"
        const val KEY_ALIAS = "mKeystoreSecretAlias"
        const val KEY_FIRST_VIDEO = "KEY_FIRST_VIDEO"
    }
}

