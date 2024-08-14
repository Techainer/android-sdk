package com.liveness.sdk.core.utils

import android.content.Context
import android.os.Build
import android.util.Log
import com.liveness.sdk.core.api.HttpClientUtilsBio.Companion.instance
import com.liveness.sdk.core.jws.TOTPGeneratorBio
import org.json.JSONObject
import java.util.UUID

internal class TotpUtilsBio(private val mContext: Context) {
    private var secret: String = ""

    init {
        secret = AppPreferenceUtilsBio(mContext).getTOTPSecret(mContext)
    }

    private fun setSecret(secretString: String) {
        secret = secretString
        Log.d("Thuytv", "------setSecret: $secretString")
        AppPreferenceUtilsBio(mContext).setTOTPSecret(mContext, secretString)
    }

    private var totpSecret: String? = null
        /**
         * return totpSecret read from shared pref
         * if null, then will try to call register device api to get secret
         * can only be call after HttpClientUtil.getInstance().setVariables called(normally called when initialize IOKyc)
         *
         * @return
         */
        private get() {
            if (secret.isNotEmpty()) {
                return secret
            }
            try {
                var a = AppPreferenceUtilsBio(mContext).getTOTPSecret(mContext) ?: AppConfigBio.mLivenessRequest?.secret
                if (a.isNullOrEmpty() || a.length != 16) {
                    a = AppUtilsBio.getSecretValue()
                }
                var b = AppPreferenceUtilsBio(mContext).getDeviceId() ?: AppConfigBio.mLivenessRequest?.deviceId
                if (b.isNullOrEmpty()) {
                    b = UUID.randomUUID().toString()
                }
                val request = JSONObject()
                request.put(AppUtilsBio.decodeAndDecrypt(mContext,AppConfigBio.encrypted_deviceId), b)
                request.put(AppUtilsBio.decodeAndDecrypt(mContext,AppConfigBio.encrypted_deviceOS), "Android")
                request.put(AppUtilsBio.decodeAndDecrypt(mContext,AppConfigBio.encrypted_device_name), Build.MANUFACTURER + " " + Build.MODEL)
                request.put(AppUtilsBio.decodeAndDecrypt(mContext,AppConfigBio.encrypted_period), AppConfigBio.mLivenessRequest?.duration)
                request.put(AppUtilsBio.decodeAndDecrypt(mContext,AppConfigBio.encrypted_secret), a)
                val response = instance?.postV3(AppUtilsBio.decodeAndDecrypt(mContext,AppConfigBio.encrypted_register_device), request)
                var result: JSONObject? = null
                if (response != null && response.length > 0) {
                    result = JSONObject(response)
                }
                if (result != null && result.has("status") && result.getInt("status") == 200) {
                    setSecret(result.getString("data"))
                    AppPreferenceUtilsBio(mContext).setValueString(AppPreferenceUtilsBio.KEY_SIGNATURE, result.getString("signature"))
                } else {
                    secret = ""
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return secret
        }

    fun getTotp(): String {
        if (totpSecret?.isNotEmpty() == true) {
            val generator = TOTPGeneratorBio()
            Log.d("Thuytv", "-----totpSecret: $totpSecret")
            return generator.generateTOTP(totpSecret!!)
        }
        return ""
    }


//    companion object {
//        private var totpUtils: TotpUtils? = null
//        fun getInstance(context: Context): TotpUtils? {
//            if (totpUtils == null) {
//                totpUtils = TotpUtils(context)
//            }
//            return totpUtils
//        }
//    }
}