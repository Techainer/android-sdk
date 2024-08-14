package io.liveness.flash.core

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.mlkit.vision.face.Face
import io.liveness.flash.core.api.HttpClientUtilsBio
import io.liveness.flash.core.facedetector.FaceDetectorBio
import io.liveness.flash.core.facedetector.Frame
import io.liveness.flash.core.facedetector.LensFacingBio
import io.liveness.flash.core.model.LivenessModelBio
import io.liveness.flash.core.utils.AppConfigBio
import io.liveness.flash.core.utils.AppPreferenceUtilsBio
import io.liveness.flash.core.utils.AppUtilsBio
import io.liveness.flash.core.utils.TotpUtilsBio
import com.nimbusds.jose.shaded.gson.Gson
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.controls.Facing
import com.otaliastudios.cameraview.controls.Mode
import io.liveness.flash.core.R
import org.json.JSONObject
import java.io.File
import java.util.Random
import java.util.UUID


/**
 * Created by Thuytv on 15/04/2024.
 */
internal class MainLiveNessActivityVideoBio : Activity() {
    private val REQUEST_PERMISSION_CODE = 1231
    private var pathVideo = ""
    private var bgColor = 0
    private var isCapture = false
    private var lstBgDefault: ArrayList<Int> = arrayListOf(R.drawable.img_0, R.drawable.img_1, R.drawable.img_2, R.drawable.img_3)

    private var isFirstVideo = true
    private var typeScreen = ""
    private lateinit var cameraViewVideo: CameraView
    private lateinit var btnCapture: Button
    private lateinit var prbLoading: ProgressBar
    private lateinit var bgFullScreenDefault: ImageView
    private lateinit var llVideo: RelativeLayout
    private var mImgLiveNess: String = ""
    private var mFaceImage: String = ""

    private var permissions = arrayOf(
        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_live_ness_image)
        if (AppConfigBio.mCustomView != null) {
            findViewById<FrameLayout>(R.id.frameViewCustom).addView(AppConfigBio.mCustomView)
        }
        cameraViewVideo = findViewById(R.id.camera_view_video)
        btnCapture = findViewById(R.id.btnCapture)
        prbLoading = findViewById(R.id.prbLoading)
        bgFullScreenDefault = findViewById(R.id.bgFullScreenDefault)
        llVideo = findViewById(R.id.llVideo)

        typeScreen = intent.getStringExtra(AppConfigBio.KEY_BUNDLE_SCREEN) ?: ""
        findViewById<ImageView>(R.id.imvBack).setOnClickListener {
            finish()
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            permissions = arrayOf(
                Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        initCamera()
        if (checkPermissions()) {
            cameraViewVideo.open()
        } else {
            requestPermissions()
        }
        if (typeScreen == AppConfigBio.TYPE_SCREEN_REGISTER_FACE) {
            btnCapture.setOnClickListener {
                cameraViewVideo.takePictureSnapshot()
            }
        } else {
            btnCapture.visibility = View.GONE
        }
        AppConfigBio.mActionView?.setOnClickListener {
            Log.d("Thuytv", "-----AppConfig.mActionView---setOnClickListener")
            cameraViewVideo.takePictureSnapshot()
        }
        registerLocalBroadCast()

    }

    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val resultCamera = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            val resultRecord = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
            val resultRead = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
            val resultWrite = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            (resultCamera == PackageManager.PERMISSION_GRANTED
                    && resultRecord == PackageManager.PERMISSION_GRANTED
                    && resultRead == PackageManager.PERMISSION_GRANTED
                    && resultWrite == PackageManager.PERMISSION_GRANTED)
        } else {
            val resultCamera = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            val resultRecord = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
            resultCamera == PackageManager.PERMISSION_GRANTED && resultRecord == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this@MainLiveNessActivityVideoBio, permissions, REQUEST_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (checkPermissions()) {
//                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG).show()
                cameraViewVideo.open()
            } else {
                Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG).show()
            }
//            if (grantResults.isNotEmpty()) {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG).show()
//                    init()
//                } else {
//                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG).show()
//                }
//            }
        }
    }

    private fun initCamera() {
        apply {
//            pathVideo = Environment.getExternalStorageDirectory().toString() + "/Download/" + "VideoLiveNess" + System.currentTimeMillis() + ".mp4"
            val fileCache = File(this.cacheDir, "VideoLiveNess" + System.currentTimeMillis() + ".mp4")
            pathVideo = fileCache.absolutePath
            val lensFacing = Facing.FRONT
            setupCamera(lensFacing)
        }
    }

    private fun setupCamera(lensFacing: Facing) = apply {
        val faceDetector = FaceDetectorBio(findViewById(R.id.faceBoundsOverlayImage))
        faceDetector.setonFaceDetectionFailureListener(object : FaceDetectorBio.OnFaceDetectionResultListener {
            override fun onSuccess(faceBounds: Face) {
                super.onSuccess(faceBounds)
                if (!isCapture) {
                    isCapture = true
                    cameraViewVideo.stopVideo()
                    bgFullScreenDefault.visibility = View.VISIBLE
                    llVideo.visibility = View.GONE
                    bgColor = Random().nextInt(3)
                    bgFullScreenDefault.background = ResourcesCompat.getDrawable(resources, lstBgDefault[bgColor], this@MainLiveNessActivityVideoBio.theme)
                    Handler(Looper.myLooper()!!).postDelayed({
                        cameraViewVideo.takePictureSnapshot()
                    }, 300)

                }
            }

        })
        cameraViewVideo.facing = lensFacing
        cameraViewVideo.mode = Mode.VIDEO


        cameraViewVideo.addCameraListener(object : CameraListener() {
            override fun onCameraOpened(options: CameraOptions) {
                super.onCameraOpened(options)
                if (typeScreen != AppConfigBio.TYPE_SCREEN_REGISTER_FACE) {
                    Handler(Looper.myLooper()!!).postDelayed({
                        cameraViewVideo.takeVideoSnapshot(File(pathVideo))
                    }, 500)
                } else {
                    if (AppConfigBio.mCustomView == null) {
                        btnCapture.visibility = View.VISIBLE
                    } else {
                        btnCapture.visibility = View.GONE
                    }
                }
            }

            override fun onCameraError(exception: CameraException) {
                super.onCameraError(exception)
                AppUtilsBio.showLog("Thuytv--onCameraError")
            }

            override fun onCameraClosed() {
                super.onCameraClosed()
                AppUtilsBio.showLog("Thuytv--onCameraClosed")
            }

            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                if (typeScreen != AppConfigBio.TYPE_SCREEN_REGISTER_FACE) {
                    result.data.let {
                        mImgLiveNess = android.util.Base64.encodeToString(it, android.util.Base64.NO_PADDING)
                        callAPIGEtTOTP(mImgLiveNess, bgColor)
                    }
                    bgFullScreenDefault.visibility = View.GONE
                    llVideo.visibility = View.VISIBLE
                    isCapture = false
                    cameraViewVideo.stopVideo()
                } else {
                    result.data.let {
                        mFaceImage = android.util.Base64.encodeToString(it, android.util.Base64.NO_PADDING)
                        registerFace(mFaceImage)
                    }
                }
            }

            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)
                result.file.let {
//                    pathVideo = Base64.encodeToString(it.readBytes(),Base64.NO_PADDING)
                }
            }

        })
        if (typeScreen != AppConfigBio.TYPE_SCREEN_REGISTER_FACE) {
            cameraViewVideo.addFrameProcessor {
                faceDetector.process(
                    Frame(
                        data = it.getData(),
                        rotation = it.rotationToUser,
                        size = Size(it.size.width, it.size.height),
                        format = it.format,
                        lensFacing = if (cameraViewVideo.facing == Facing.BACK) LensFacingBio.BACK else LensFacingBio.FRONT
                    )
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        cameraViewVideo.open()
    }

    override fun onPause() {
        super.onPause()
        cameraViewVideo.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraViewVideo.destroy()
        if (AppConfigBio.mCustomView != null) {
            findViewById<FrameLayout>(R.id.frameViewCustom).removeView(AppConfigBio.mCustomView)
        }
        unregisterLocalBroadCast()
    }



    private fun initTransaction(tOTP: String, imgLiveNess: String, bgColor: Int) {
        val response = HttpClientUtilsBio.instance?.initTransaction(this)
        var result: JSONObject? = null
        if (!response.isNullOrEmpty()) {
            result = JSONObject(response)
        }
        var status = -1
        if (result?.has("status") == true) {
            status = result.getInt("status")
        }
        var strMessage = "Error"
        if (result?.has("message") == true) {
            strMessage = result.getString("message")
        }
        if (status == 200) {
            val transactionId = result?.getString("data") ?: ""
//            val signature = result?.getString("signature")
            checkLiveNessFlash(tOTP, transactionId, imgLiveNess, bgColor)
        } else {
            showToast(strMessage)
        }
    }

    private fun checkLiveNessFlash(tOTP: String, transactionID: String, imgLiveNess: String, bgColor: Int) {
        val response = HttpClientUtilsBio.instance?.checkLiveNessFlash(this, tOTP, transactionID, imgLiveNess, bgColor)
        var result: JSONObject? = null
        if (response?.isNotEmpty() == true) {
            result = JSONObject(response)
        }
        var status = -1
        if (result?.has("status") == true) {
            status = result.getInt("status")
        }
        var strMessage = "Error"
        if (result?.has("message") == true) {
            strMessage = result.getString("message")
        }
        AppUtilsBio.showLog("result: "+ result?.toString())
        if (status == 200) {
            val liveNessModel = Gson().fromJson<LivenessModelBio>(response, LivenessModelBio::class.java)
//            if (liveNessModel.success == true) {
            liveNessModel.pathVideo = pathVideo
            liveNessModel.livenessImage = mImgLiveNess
            this.runOnUiThread {
                showLoading(false)
                AppUtilsBio.showLog("Thuytv------pathVideo: $pathVideo")
                AppConfigBio.livenessListener?.onCallbackLiveness(liveNessModel)
                finish()
            }
//            } else {
//                showToast(result?.getString("message") ?: "Error")
//            }
        } else {
//            showToast(result?.getString("message") ?: "Error")
            this.runOnUiThread {
                showLoading(false)
                AppConfigBio.livenessListener?.onCallbackLiveness(LivenessModelBio(status = status, message = strMessage))
                finish()
            }
        }
    }

    fun callAPIGEtTOTP(imgLiveNess: String, bgColor: Int) {
        showLoading(true)
        Thread {
            val tOTP = TotpUtilsBio(this).getTotp()
            if (tOTP.isNullOrEmpty() || tOTP == "-1") {
                showToast("TOTP null")
            } else {
                initTransaction(tOTP, imgLiveNess, bgColor)
            }
        }.start()
    }

    private fun showToast(strToast: String) {
        this.runOnUiThread {
            Toast.makeText(this, strToast, Toast.LENGTH_SHORT).show()
            showLoading(false)
        }
    }

    private fun showLoading(isShow: Boolean) {
        this.runOnUiThread {
            if (isShow) {
                if (AppConfigBio.mProgressView == null) {
                    prbLoading.visibility = View.VISIBLE
                } else {
                    AppConfigBio.mProgressView?.visibility = View.VISIBLE
                }

            } else {
                if (AppConfigBio.mProgressView == null) {
                    prbLoading.visibility = View.GONE
                } else {
                    AppConfigBio.mProgressView?.visibility = View.GONE
                }
            }
        }
    }

    private fun registerFace(faceImage: String) {
        showLoading(true)
        Thread {
            var mSecret = AppPreferenceUtilsBio(this).getTOTPSecret(this) ?: AppConfigBio.mLivenessRequest?.secret
            if (mSecret.isNullOrEmpty() || mSecret.length != 16) {
                mSecret = AppUtilsBio.getSecretValue()
            }
            var mDeviceId = AppPreferenceUtilsBio(this).getDeviceId() ?: AppConfigBio.mLivenessRequest?.deviceId
            if (mDeviceId.isNullOrEmpty()) {
                mDeviceId = UUID.randomUUID().toString()
            }
            val request = JSONObject()
            request.put("deviceId", mDeviceId)
            request.put("deviceOs", "Android")
            request.put("deviceName", Build.MANUFACTURER + " " + Build.MODEL)
            request.put("period", AppConfigBio.mLivenessRequest?.duration)
            request.put("secret", mSecret)
            val responseDevice = HttpClientUtilsBio.instance?.postV3("/eid/v3/registerDevice", request)
            var result: JSONObject? = null
            if (responseDevice != null && responseDevice.length > 0) {
                result = JSONObject(responseDevice)
            }
            var statusDevice = -1
            if (result?.has("status") == true) {
                statusDevice = result.getInt("status")
            }
            var strMessageDevice = "Error"
            if (result?.has("message") == true) {
                strMessageDevice = result.getString("message")
            }
            if (statusDevice == 200) {
                AppPreferenceUtilsBio(this).setDeviceId(mDeviceId)
                AppPreferenceUtilsBio(this).setTOTPSecret(this, mSecret)
                val response = HttpClientUtilsBio.instance?.registerFace(this, faceImage)
                var result: JSONObject? = null
                if (response?.isNotEmpty() == true) {
                    result = JSONObject(response)
                }
                var status = -1
                if (result?.has("status") == true) {
                    status = result.getInt("status")
                }
                var strMessage = "Error"
                if (result?.has("message") == true) {
                    strMessage = result.getString("message")
                }
                if (status == 200) {
                    showLoading(false)
                    this.runOnUiThread {
                        AppConfigBio.livenessFaceListener?.onCallbackLiveness(LivenessModelBio(faceImage = mFaceImage))
                        showToast("Register Face Success")
                        AppPreferenceUtilsBio(this).setRegisterFace(true)
                        finish()
                    }

                } else {
                    showLoading(false)
//                    showToast(result?.getString("message") ?: "Error")
                    this.runOnUiThread {
                        AppConfigBio.livenessFaceListener?.onCallbackLiveness(LivenessModelBio(status = status, message = strMessage))
                        finish()
                    }
                }
            } else {
                this.runOnUiThread {
                    AppConfigBio.livenessFaceListener?.onCallbackLiveness(LivenessModelBio(status = statusDevice, message = strMessageDevice))
                    finish()
                }
            }
        }.start()

    }

    private fun registerLocalBroadCast() {
        val filter = IntentFilter()
        filter.addAction(AppConfigBio.INTENT_VALUE_BACK)
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiverOnBack, filter)
    }

    private fun unregisterLocalBroadCast() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverOnBack)
    }

    private var receiverOnBack = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            if (intent?.action == AppConfigBio.INTENT_VALUE_BACK && !isFinishing) {
                finish()
            }
        }

    }
}