package com.liveness.sdk.core

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.Keep
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.liveness.sdk.core.api.HttpClientUtilsBio
import com.liveness.sdk.core.model.LivenessRequestBio
import com.liveness.sdk.core.utils.AppConfigBio
import com.liveness.sdk.core.utils.AppPreferenceUtilsBio
import com.liveness.sdk.core.utils.CallbackAPIListenerBio
import com.liveness.sdk.core.utils.CallbackLivenessListenerBio

/**
 * Created by Thuytv on 15/04/2024.
 */
@Keep
class LiveNessSDKBio {
    companion object {
        @Keep
        fun setConfigSDK(context: Context, mLivenessRequest: LivenessRequestBio? = null) {
            if (mLivenessRequest != null) {
                AppConfigBio.mLivenessRequest = mLivenessRequest
            }
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.setVariables(context, mLivenessRequest)
//            testRSA()
//            val enCryptData = EnCryptData()
//            enCryptData.encryptText(context, AppConfig.key_encrypted_init_transaction, AppConfig.encrypted_init_transaction)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_register_device, AppConfig.encrypted_register_device)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_register_face, AppConfig.encrypted_register_face)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_verify_face, AppConfig.encrypted_verify_face)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_deviceId, AppConfig.encrypted_deviceId)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_deviceOS, AppConfig.encrypted_deviceOS)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_device_name, AppConfig.encrypted_device_name)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_period, AppConfig.encrypted_period)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_secret, AppConfig.encrypted_secret)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_face_image, AppConfig.encrypted_face_image)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_device_id, AppConfig.encrypted_device_id)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_totp, AppConfig.encrypted_totp)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_transaction_id, AppConfig.encrypted_transaction_id)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_image_live, AppConfig.encrypted_image_live)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_color, AppConfig.encrypted_color)
//            enCryptData.encryptText(context, AppConfig.key_encrypted_transaction_id, AppConfig.encrypted_transaction_id)

        }

        @Keep
        fun registerDevice(context: Context, mRequest: LivenessRequestBio? = null, callbackAPIListener: CallbackAPIListenerBio?) {
            if (mRequest != null) {
                AppConfigBio.mLivenessRequest = mRequest
            }
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.registerDevice(context, callbackAPIListener)
        }

        @Keep
        fun registerFace(context: Context, faceImage: String, mRequest: LivenessRequestBio? = null, callbackAPIListener: CallbackAPIListenerBio?) {
            if (mRequest != null) {
                AppConfigBio.mLivenessRequest = mRequest
                AppConfigBio.mOptionRequest = mRequest
            }
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.registerFace(context, faceImage, callbackAPIListener)
        }

        @Keep
        fun initTransaction(context: Context, mRequest: LivenessRequestBio? = null, callbackAPIListener: CallbackAPIListenerBio?) {
            if (mRequest != null) {
                AppConfigBio.mOptionRequest = mRequest
                AppConfigBio.mLivenessRequest = mRequest
            }
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.initTransaction(context, callbackAPIListener)
        }

        @Keep
        fun checkLiveNessFlash(
            context: Context,
            transactionId: String,
            liveImage: String,
            colorBg: Int,
            mRequest: LivenessRequestBio? = null,
            callbackAPIListener: CallbackAPIListenerBio?
        ) {
            if (mRequest != null) {
                AppConfigBio.mOptionRequest = mRequest
                AppConfigBio.mLivenessRequest = mRequest
            }
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.checkLiveNessFlash(context, transactionId, liveImage, colorBg, callbackAPIListener)
        }

        @Keep
        fun checkLiveNess(
            context: Context,
            liveImage: String,
            colorBg: Int,
            mRequest: LivenessRequestBio? = null,
            callbackAPIListener: CallbackAPIListenerBio?
        ) {
            if (mRequest != null) {
                AppConfigBio.mOptionRequest = mRequest
                AppConfigBio.mLivenessRequest = mRequest
            }
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.checkLiveNess(context, liveImage, colorBg, callbackAPIListener)
        }

        @Keep
        fun startLiveNess(
            context: Context,
            mLivenessRequest: LivenessRequestBio? = null,
            supportFragmentManager: FragmentManager,
            frameView: Int,
            livenessListener: CallbackLivenessListenerBio?
        ) {
            livenessListener?.let {
                AppConfigBio.livenessListener = it
            }
            if (mLivenessRequest != null) {
                AppConfigBio.mLivenessRequest = mLivenessRequest
            }
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.setVariables(context, mLivenessRequest)
            if (mLivenessRequest?.isVideo == true) {
                val intent = Intent(context, MainLiveNessActivityVideoBio::class.java)
                intent.putExtra(AppConfigBio.KEY_BUNDLE_SCREEN, "")
                ContextCompat.startActivity(context, intent, null)
            } else {
                val transaction = supportFragmentManager.beginTransaction()
                val bundle = Bundle()
                bundle.putString(AppConfigBio.KEY_BUNDLE_SCREEN, "")
                val fragment = MainLiveNessImageActivity()
                fragment.setFragmentManager(supportFragmentManager)
                fragment.arguments = bundle
                transaction.replace(frameView, fragment)
                transaction.addToBackStack(MainLiveNessImageActivity::class.java.name)
                transaction.commit()
            }

        }

        @Keep
        fun setCustomView(mCustomView: View, mActionView: View?) {
            AppConfigBio.mCustomView = mCustomView
            AppConfigBio.mActionView = mActionView
        }

        @Keep
        fun setCustomProgress(mProgressView: View?) {
            AppConfigBio.mProgressView = mProgressView
        }

        @Keep
        fun setCallbackListener(livenessListener: CallbackLivenessListenerBio?) {
            livenessListener?.let {
                AppConfigBio.livenessListener = it
            }
        }

        @Keep
        fun setCallbackListenerFace(livenessListener: CallbackLivenessListenerBio?) {
            livenessListener?.let {
                AppConfigBio.livenessFaceListener = it
            }
        }

        @Keep
        fun clearAllData(context: Context) {
            AppPreferenceUtilsBio(context).removeAllValue()
        }

        @Keep
        fun registerFace(context: Context, mLivenessRequest: LivenessRequestBio? = null, livenessListener: CallbackLivenessListenerBio?) {
            livenessListener?.let {
                AppConfigBio.livenessFaceListener = it
            }
            AppConfigBio.mLivenessRequest = mLivenessRequest
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.setVariables(context, mLivenessRequest)
            if (mLivenessRequest?.imageFace.isNullOrEmpty()) {
//                if (mLivenessRequest.isVideo) {
                val intent = Intent(context, MainLiveNessActivityVideoBio::class.java)
                intent.putExtra(AppConfigBio.KEY_BUNDLE_SCREEN, AppConfigBio.TYPE_SCREEN_REGISTER_FACE)
                ContextCompat.startActivity(context, intent, null)
//                }
            } else {
                httpClientUtil?.registerDeviceAndFace(context, mLivenessRequest?.imageFace ?: "")
            }
        }

        @Keep
        fun registerFace(
            context: Context, mLivenessRequest: LivenessRequestBio? = null, supportFragmentManager: FragmentManager,
            frameView: Int, livenessListener: CallbackLivenessListenerBio?
        ) {
            livenessListener?.let {
                AppConfigBio.livenessFaceListener = it
            }
            AppConfigBio.mLivenessRequest = mLivenessRequest
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.setVariables(context, mLivenessRequest)
            if (mLivenessRequest?.imageFace.isNullOrEmpty()) {
                val transaction = supportFragmentManager.beginTransaction()
                val bundle = Bundle()
                bundle.putString(AppConfigBio.KEY_BUNDLE_SCREEN, AppConfigBio.TYPE_SCREEN_REGISTER_FACE)
                val fragment = MainLiveNessImageActivity()
                fragment.setFragmentManager(supportFragmentManager)
                fragment.arguments = bundle
                transaction.add(frameView, fragment)
                transaction.addToBackStack(MainLiveNessImageActivity::class.java.name)
                transaction.commit()
            } else {
                httpClientUtil?.registerDeviceAndFace(context, mLivenessRequest?.imageFace ?: "")
            }
        }

        @Keep
        fun registerFace(
            context: Context, imageFace: String,
            livenessListener: CallbackLivenessListenerBio?
        ) {
            livenessListener?.let {
                AppConfigBio.livenessFaceListener = it
            }
            val httpClientUtil = HttpClientUtilsBio.instance
            httpClientUtil?.registerDeviceAndFace(context, imageFace ?: "")
        }

        @Keep
        fun getDeviceId(context: Context): String? {
            return AppPreferenceUtilsBio(context).getDeviceId()
        }

        @Keep
        fun isRegisterFace(context: Context): Boolean {
            return AppPreferenceUtilsBio(context).isRegisterFace()
        }

//        fun getDeviceId2(context: Context): String? {
//            return AppPreferenceUtils(context).getDeviceId2()
//        }
//
//        fun getTOTPSecret2(context: Context): String? {
//            return KeyStoreUtils.getInstance(context)?.decryptData(AppPreferenceUtils(context).getTOTPSecret())
//        }

        @Keep
        fun getLivenessRequest(): LivenessRequestBio? {
            return AppConfigBio.mLivenessRequest
        }

        @Keep
        fun checkVersion(): String {
            return "v1.1.7"
        }

    }
}