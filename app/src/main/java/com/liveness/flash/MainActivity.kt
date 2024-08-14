package com.liveness.flash

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import io.liveness.flash.core.LiveNessSDKBio
import io.liveness.flash.core.model.LivenessModelBio
import io.liveness.flash.core.model.LivenessRequestBio
import io.liveness.flash.core.utils.CallbackLivenessListenerBio
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Created by Thuytv on 15/04/2024.
 */
class MainActivity : AppCompatActivity() {
    private var deviceId = ""
    private lateinit var btnRegisterFace: Button
    private lateinit var btnLiveNessFlash: Button
    private lateinit var videoLiveness: VideoView
    private lateinit var imvImage: ImageView
    private lateinit var viewManager: FrameLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_main_activity)
        btnRegisterFace = findViewById(R.id.btn_register_face)
        btnLiveNessFlash = findViewById(R.id.btn_live_ness_flash)
        videoLiveness = findViewById(R.id.video_liveness)
//        videoLiveness.visibility = View.GONE
        viewManager = findViewById(R.id.frame_view_main)

//        imvImage = findViewById(R.id.imv_image)

//        val mDeviceId = LiveNessSDK.getDeviceId(this)
//        if (mDeviceId?.isNotEmpty() == true) {
//            deviceId = mDeviceId
//            btnRegisterFace.isEnabled = false
//        }
//        if(LiveNessSDK.isRegisterFace(this))

        LiveNessSDKBio.setCallbackListener(object : CallbackLivenessListenerBio {
            override fun onCallbackLiveness(data: LivenessModelBio?) {
                if (data?.action == 8) {
                    Log.d("Thuytv", "CallbackListener: ${data.action}")
                    if (data?.pathVideo?.isNotEmpty() == true) {
                        Log.d("Thuytv", "pathVideo:${data?.pathVideo}")
                        Log.d("Thuytv", "fileVideo length: ${File(data?.pathVideo).length()}")
//                        val fileVideo = File(this@MainActivity.cacheDir, "VideoLiveNess")
//                        if (fileVideo.exists()) {
//                            Log.d("Thuytv", "fileVideo.exists(): ${fileVideo?.length()}")
//                            for (f in fileVideo.listFiles()) {
//                                Log.d("Thuytv", "fileVideo name: ${f.name}")
//                                Log.d("Thuytv", "fileVideo length: ${f.length()}")
//                                this@MainActivity.runOnUiThread {
//                                    viewManager.visibility = View.GONE
//                                    videoLiveness.setVideoPath(f.path)
//                                    videoLiveness.start()
//                                }
//                            }
//                        }
//                        else Log.d("Thuytv", "fileVideo not exists():")

                        if (data.pathVideo != null && data.pathVideo != "") {
                            this@MainActivity.runOnUiThread {
                                viewManager.visibility = View.GONE
                                videoLiveness.setVideoPath(data.pathVideo)
                                videoLiveness.start()
                            }
                        }
                    }
                }

//                val decodeString = android.util.Base64.decode(data?.livenessImage ?: "", android.util.Base64.NO_PADDING)
//                val bitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
//                imvImage.setImageBitmap(bitmap)
//                showDefaultDialog(this@MainActivity, data?.data?.toString())
            }
        })
        btnLiveNessFlash.setOnClickListener {
//            val overlay = LayoutInflater.from(this).inflate(R.layout.ui_custom_view, null)
//            LiveNessSDK.setCustomProgress(overlay)
            viewManager.visibility = View.VISIBLE
            LiveNessSDKBio.startLiveNess(
                this, getLivenessRequest(),
                supportFragmentManager, viewManager.id, null)


//            val transaction = supportFragmentManager.beginTransaction()
//            val fragment = MainFragment()
//            transaction.replace(R.id.frame_view_main, fragment)
//            transaction.addToBackStack(MainFragment::class.java.name)
//            transaction.commit()

//            val transaction = supportFragmentManager.beginTransaction()
//            val fragment = FaceFragment()
//            val bundle = Bundle()
//            bundle.putString("KEY_BUNDLE_SCREEN", "")
//            fragment?.arguments = bundle
//            transaction.replace(R.id.frame_view_main, fragment)
//            transaction.addToBackStack(FaceFragment::class.java.name)
//            transaction.commit()

//            LiveNessSDK.testRSA()

//            val encrypted_register_face = encryptAndEncode("/eid/v3/registerFace")
//            val encrypted_init_transaction = encryptAndEncode("/eid/v3/initTransaction")
//            val encrypted_register_device = encryptAndEncode("/eid/v3/registerDevice")
//            val encrypted_verify_face = encryptAndEncode("/eid/v3/verifyFace")
//            Log.d("Thuytv","-----encrypted_register_face: $encrypted_register_face")
//            Log.d("Thuytv","-----encrypted_init_transaction: $encrypted_init_transaction")
//            Log.d("Thuytv","-----encrypted_register_device: $encrypted_register_device")
//            Log.d("Thuytv","-----encrypted_verify_face: $encrypted_verify_face")
//            val decryptedRegisterFace = decodeAndDecrypt(encrypted_register_face!!)
//            val decryptedInitTransaction = decodeAndDecrypt(encrypted_init_transaction!!)
//            val decryptedRegisterDevice = decodeAndDecrypt(encrypted_register_device!!)
//            val decryptedVerifyFace = decodeAndDecrypt(encrypted_verify_face!!)
//            Log.d("Thuytv","-----decryptedRegisterFace: $decryptedRegisterFace")
//            Log.d("Thuytv","-----decryptedInitTransaction: $decryptedInitTransaction")
//            Log.d("Thuytv","-----decryptedRegisterDevice: $decryptedRegisterDevice")
//            Log.d("Thuytv","-----decryptedVerifyFace: $decryptedVerifyFace")

        }
        btnRegisterFace.setOnClickListener {
//            val overlay = LayoutInflater.from(this).inflate(R.layout.ui_register_face, null)
//            LiveNessSDK.setCustomView(overlay, overlay.findViewById(R.id.btn_capture_face))
//            val request = getLivenessRequest()
////            request.imageFace = getImage()
//            LiveNessSDK.registerFace(this, request, supportFragmentManager, R.id.frame_view_main, object : CallbackLivenessListener {
//                override fun onCallbackLiveness(data: LivenessModel?) {
//                    Log.d("Thuytv", "------faceImage: ${data?.message}")
//                    btnRegisterFace.isEnabled = false
//
//                    val decodeString = android.util.Base64.decode(data?.faceImage ?: "", android.util.Base64.NO_PADDING)
//
//                    val bitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
//                    imvImage.setImageBitmap(bitmap)
//                }
//            })

//            val transaction = supportFragmentManager.beginTransaction()
//            val fragment = FaceFragment()
//            val bundle = Bundle()
//            bundle.putString("KEY_BUNDLE_SCREEN", "TYPE_SCREEN_REGISTER_FACE")
//            fragment?.arguments = bundle
//            transaction.replace(R.id.frame_view_main, fragment)
//            transaction.addToBackStack(FaceFragment::class.java.name)
//            transaction.commit()

        }

    }
    fun encryptAndEncode(data: String): String? {
        return Base64.encodeToString(data.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
    }

    fun decodeAndDecrypt(data: String): String? {
        return String(Base64.decode(data, Base64.DEFAULT))
    }

    private fun getLivenessRequest(): LivenessRequestBio {
        val privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCtZW3L3TiJ7+Hb\n" +
                "KlEXd3pkjKiwjk9fZgmFAWzKirkjdBHS9YnM7tp8tUNIHGb87o6ZjkcnNOhbdlRw\n" +
                "L8Zl/eAu1J7UQ85smzw+5L36wsQB9YHQw4dikWin7M355qNn0IIxhWA3iqUFEhWN\n" +
                "czYco+W6+w59IAtYVRmRnDUUC/UjPdPYnwd3rz7i0Hea4r7Q/4AicDVN6p3duIwg\n" +
                "+SDVxJomlpsdc+fj/3oOrfdvUS00VVngUUOlswSnSMvKcRmM8NKcaSGKq2bt9AxN\n" +
                "iZsI+4mjWfnsYq0Ms5RQBBF0xju/rWez664x/KVX7j9XhCvq8BGflJ91donYFuMA\n" +
                "Xgy5q5NjAgMBAAECggEAIswYHLFoh0X8rV7wpyTzCvqvX78vbpWrk2WVz4/HV7YT\n" +
                "XaKo5NeKQTyfI/mPMXMuauKCpPuZJcG5cEomJpGsS7mfpjl1U5ZToMuG1KwBaeM7\n" +
                "CgozQTStLAX50AzY/hx6BDYf+QV52GqoqJpWYakCkWOQpMupezCY0P/oJv2/VDLf\n" +
                "lbGNN1ZvscK+sf80UyvdWArNI54TtD4DbSj3n0O67qd3S4JACAV1V4yOhPQMOrpk\n" +
                "IlnKpOS2jUMq1JJEzt3msLmXx1LaIB2wte4DqDlwd7XW2XdX3hSY27o4y24Axa6N\n" +
                "WnCc/HymM+LwkXWYqgWQhh+ey7JtTF2et29UsX/ZAQKBgQDZdiqKCWCF8e2Ndgbk\n" +
                "jtvEov9kWLvsqJo6x391/cBoa4SEiO7xS8D+Lm+Ym8SuffnCWOKqMZxnmVnDUHwf\n" +
                "h8LF+k69LydMP8vKCKzD0LGN0EZ6xup8D6m8jU3omT2yZGssyoxXYQznUcn5CViY\n" +
                "L9eQVXw4YLSe63D9VIqMxm9/ZQKBgQDMIBjfDHIW1b53wT2FMLIMZR8eiTUXAHPQ\n" +
                "Mp2zZ14AKoSUp1THDnuTmnHzWgn6/7pKWz+hk45LG8JXHnOMX3GFzvG9aQMkRuuD\n" +
                "Sd7VipCCp1o4dsoSrHIJ5TiQJOZaQgureesHdOAeP0STxGVOmX7DFhUxSzwjAZz2\n" +
                "FDHwrHNPJwKBgQDN/1Q4wr0+5XiE4uOQq4uf8FBCPJR4kRbYy5cArMoRoJg9/IFs\n" +
                "7rf5kP+B7z0Xlpp78jt1wd1Jfkk77ghGzhJB/OWN7Rcq8dwYnLMcI5uunTfGopwJ\n" +
                "vcSqqqi8yD1buiiUm6LqOzNABYhwctwL/nYTcgdkWKeBS8MTF3zP8kI4yQKBgQCl\n" +
                "+IcgfPca+ApZRuclr7VlfKcz5e4j2LtSEoXFRIva6LdKQ1AcVftGxbJXYuNwkZPA\n" +
                "N7diQh7VlSmMOndLMKOWX/CQyJzEV2HRKzQjPvpHMZmbBYNCcbJ7t0Qpd8dQphjl\n" +
                "AUmHk5FTJrA00eBpa0b1irQKk5i/AeXE9CCzBxTuywKBgA3wbWbuhSe/kL3QOuuG\n" +
                "GBtH5E7FsHQ++PphMcdpeSVaA6yiVc/3Iu2d+p+Nda3VGiqD6/uVVRWDxcE3u3eQ\n" +
                "88Xwl6yudl9moadaaYydjWB3wIP9lZKW7MzV86HJnXvr4JNsre0JqO+OBDSI3YUb\n" +
                "8Ywxgfrp5wx1/7VYTLOdROUh\n" +
                "-----END PRIVATE KEY-----"
        val public_key = "-----BEGIN CERTIFICATE-----\n" +
                "MIIE8jCCA9qgAwIBAgIQVAESDxKv/JtHV15tvtt1UjANBgkqhkiG9w0BAQsFADAr\n" +
                "MQ0wCwYDVQQDDARJLUNBMQ0wCwYDVQQKDARJLUNBMQswCQYDVQQGEwJWTjAeFw0y\n" +
                "MzA2MDcwNjU1MDNaFw0yNjA2MDkwNjU1MDNaMIHlMQswCQYDVQQGEwJWTjESMBAG\n" +
                "A1UECAwJSMOgIE7hu5lpMRowGAYDVQQHDBFRdeG6rW4gSG/DoG5nIE1haTFCMEAG\n" +
                "A1UECgw5Q8OUTkcgVFkgQ1AgROG7ikNIIFbhu6QgVsOAIEPDlE5HIE5HSOG7hiBT\n" +
                "4buQIFFVQU5HIFRSVU5HMUIwQAYDVQQDDDlDw5RORyBUWSBDUCBE4buKQ0ggVuG7\n" +
                "pCBWw4AgQ8OUTkcgTkdI4buGIFPhu5AgUVVBTkcgVFJVTkcxHjAcBgoJkiaJk/Is\n" +
                "ZAEBDA5NU1Q6MDExMDE4ODA2NTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoC\n" +
                "ggEBAJO6JDU+kNEUIiO6m75LOfgHkwGExYFv0tILHInS9CkK2k0FjmvU8VYJ0cQA\n" +
                "sGGabpHIwfh07llLfK3TUZlhnlFZYRrYvuexlLWQydjHYPqT+1c3iYaiXXcOqEjm\n" +
                "OupCj71m93ThFrYzzI2Zx07jccRptAAZrWMjI+30vJN7SDxhYsD1uQxYhUkx7psq\n" +
                "MqD4/nOyaWzZHLU94kTAw5lhAlVOMu3/6pXhIltX/097Wji1eyYqHFu8w7q3B5yW\n" +
                "gJYugEZfplaeLLtcTxok4VbQCb3cXTOSFiQYJ3nShlBd89AHxaVE+eqJaMuGj9z9\n" +
                "rdIoGr9LHU/P6KF+/SLwxpsYgnkCAwEAAaOCAVUwggFRMAwGA1UdEwEB/wQCMAAw\n" +
                "HwYDVR0jBBgwFoAUyCcJbMLE30fqGfJ3KXtnXEOxKSswgZUGCCsGAQUFBwEBBIGI\n" +
                "MIGFMDIGCCsGAQUFBzAChiZodHRwczovL3Jvb3RjYS5nb3Yudm4vY3J0L3ZucmNh\n" +
                "MjU2LnA3YjAuBggrBgEFBQcwAoYiaHR0cHM6Ly9yb290Y2EuZ292LnZuL2NydC9J\n" +
                "LUNBLnA3YjAfBggrBgEFBQcwAYYTaHR0cDovL29jc3AuaS1jYS52bjA0BgNVHSUE\n" +
                "LTArBggrBgEFBQcDAgYIKwYBBQUHAwQGCisGAQQBgjcKAwwGCSqGSIb3LwEBBTAj\n" +
                "BgNVHR8EHDAaMBigFqAUhhJodHRwOi8vY3JsLmktY2Eudm4wHQYDVR0OBBYEFE6G\n" +
                "FFM4HXne9mnFBZInWzSBkYNLMA4GA1UdDwEB/wQEAwIE8DANBgkqhkiG9w0BAQsF\n" +
                "AAOCAQEAH5ifoJzc8eZegzMPlXswoECq6PF3kLp70E7SlxaO6RJSP5Y324ftXnSW\n" +
                "0RlfeSr/A20Y79WDbA7Y3AslehM4kbMr77wd3zIij5VQ1sdCbOvcZXyeO0TJsqmQ\n" +
                "b46tVnayvpJYW1wbui6smCrTlNZu+c1lLQnVsSrAER76krZXaOZhiHD45csmN4dk\n" +
                "Y0T848QTx6QN0rubEW36Mk6/npaGU6qw6yF7UMvQO7mPeqdufVX9duUJav+WBJ/I\n" +
                "Y/EdqKp20cAT9vgNap7Bfgv5XN9PrE+Yt0C1BkxXnfJHA7L9hcoYrknsae/Fa2IP\n" +
                "99RyIXaHLJyzSTKLRUhEVqrycM0UXg==\n" +
                "-----END CERTIFICATE-----\n"
        val appId = "com.pvc.test"
//        if (deviceId.isNullOrEmpty()) {
//            deviceId = UUID.randomUUID().toString()
//        }
//        deviceId = "f8552f6d-35da-45f0-9761-f38fe1ea33d1"
        if (LiveNessSDKBio.getDeviceId(this)?.isNotEmpty() == true) {
            deviceId = LiveNessSDKBio.getDeviceId(this).toString()
        }
//        val optionHeader: HashMap<String, String> = HashMap()
//        optionHeader["header1"] = "test"
//        optionHeader["header2"] = "TEST-02"
//        val optionRequest: HashMap<String, String> = HashMap()
//        optionRequest["request-1"] = "test"
//        optionRequest["request-2"] = "TEST-02"

//        appId={'com.appota.test'}
//        baseUrl={'https://ekyc-sandbox.eidas.vn/face-matching'}

        //ABCDEFGHIJKLMNOP
        return LivenessRequestBio(
            duration = 600, privateKey = privateKey, appId = appId,
            clientTransactionId = "", deviceId = deviceId,
            baseURL = "https://ekyc-sandbox.eidas.vn/face-matching", publicKey = public_key,
            isDebug = true
        )
    }

    private fun showDefaultDialog(context: Context, strContent: String?) {
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply {
            //setIcon(R.drawable.ic_hello)
            setTitle("Success")
            setMessage(strContent)
            setPositiveButton("OK") { _: DialogInterface?, _: Int ->
            }

        }.create().show()
    }

    private fun getImage(): String {
        val bitmap = BitmapFactory.decodeResource(this.getResources(), io.liveness.flash.core.R.drawable.img_0)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        return android.util.Base64.encodeToString(image, android.util.Base64.NO_PADDING)
    }
}