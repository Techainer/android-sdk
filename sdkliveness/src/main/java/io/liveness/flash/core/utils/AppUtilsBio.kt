package io.liveness.flash.core.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random
import java.util.UUID


/**
 * Created by Thuytv on 16/04/2024.
 */
internal object AppUtilsBio {
    fun showLog(strLog: String) {
//        if (BuildConfig.DEBUG) {
        if (AppConfigBio.mLivenessRequest?.isDebug == true) {
            Log.d("LiveNess", strLog)
        }
//        }
    }

    fun getDeviceId(context: Context): String {
        val appPreferenceUtils = AppPreferenceUtilsBio(context)
        var deviceId = appPreferenceUtils.getValueString(AppPreferenceUtilsBio.KEY_DEVICE_ID)
        if (deviceId.isNullOrEmpty()) {
            deviceId = UUID.randomUUID().toString()
            appPreferenceUtils.setValueString(AppPreferenceUtilsBio.KEY_DEVICE_ID, deviceId)
        }
        return deviceId
    }

    fun saveVideoToInternalStorage(currentFile: File, context: Context) {
        val newfile: File
        try {
            val fileName = currentFile.name
            val cw = ContextWrapper(context)
            val directory = cw.getDir("videoDir", Context.MODE_PRIVATE)
            newfile = File(directory, fileName)
            if (currentFile.exists()) {
                val `in`: InputStream = FileInputStream(currentFile)
                val out: OutputStream = FileOutputStream(newfile)

                // Copy the bits from instream to outstream
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
                `in`.close()
                out.close()
                Log.v("", "Video file saved successfully.")
            } else {
                Log.v("", "Video saving failed. Source file missing.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun captureScreenshot(myRootView: View): Bitmap? {
        // We need date and time to be added to image name to make it unique every time, otherwise bitmap will not update
        val bitmap = Bitmap.createBitmap(myRootView.width, myRootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = myRootView.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.WHITE)
        }
        myRootView.draw(canvas)
        return bitmap
    }

    fun shareScreenshot(mContext: Context, myRootView: View): Uri {
        // We need date and time to be added to image name to make it unique every time, otherwise bitmap will not update
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val currentDateandTime = sdf.format(Date())
        val imageName = "/image_$currentDateandTime.jpg"

        // CREATE
//        myRootView.isDrawingCacheEnabled = true
//        myRootView.buildDrawingCache(true) // maybe You dont need this
//        val bitmap = Bitmap.createBitmap(myRootView.drawingCache)
//        myRootView.isDrawingCacheEnabled = false
        val bitmap =
            Bitmap.createBitmap(myRootView.width, myRootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        myRootView.draw(canvas)
        // SAVE
        try {
            File(mContext.cacheDir, "images").deleteRecursively() // delete old images
            val cachePath = File(mContext.cacheDir, "images")
            cachePath.mkdirs() // don't forget to make the directory
            val stream = FileOutputStream("$cachePath$imageName")
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                90,
                stream
            ) // can be png and any quality level
            stream.close()
        } catch (ex: Exception) {
            Toast.makeText(mContext, ex.javaClass.canonicalName, Toast.LENGTH_LONG)
                .show() // You can replace this with Log.e(...)
        }

        // SHARE
        val imagePath = File(mContext.cacheDir, "images")
        val newFile = File(imagePath, imageName)
        val contentUri =
            FileProvider.getUriForFile(mContext, getPackageName(mContext) + ".provider", newFile)
        return contentUri
    }

    fun getPackageName(context: Context): String {
        var mPackageName = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            mPackageName = pInfo.packageName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return mPackageName
    }

    fun getSecretValue(): String {
        val LATIN_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val random = Random()
        val sb = StringBuilder(16)
        for (i in 0..15) {
            val index = random.nextInt(LATIN_LETTERS.length)
            sb.append(LATIN_LETTERS[index])
        }
        return sb.toString()
    }

    fun encryptAndEncode(data: String): String? {
        return Base64.encodeToString(data.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
    }

    private fun decodeAndDecrypt(data: String): String {
        val rsa = RSACryptDataBio()
        return rsa.decrypt(data)
    }

    fun decodeAndDecrypt(context: Context, data: String): String {
//        val strEncryted = AppPreferenceUtils(context).getValueString(key)
//        val deCryptData = DeCryptData()
//        val data = deCryptData.decryptData(context, key)
        val rsa = RSACryptDataBio()
        return rsa.decrypt(data)
    }

}