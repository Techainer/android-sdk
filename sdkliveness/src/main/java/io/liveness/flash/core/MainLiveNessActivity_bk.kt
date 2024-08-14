//package com.liveness.sdk.core
//
//import android.Manifest
//import android.app.Activity
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.os.Handler
//import android.os.Looper
//import android.util.Size
//import android.view.View
//import android.widget.Button
//import android.widget.FrameLayout
//import android.widget.ImageView
//import android.widget.ProgressBar
//import android.widget.RelativeLayout
//import android.widget.Toast
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.core.content.res.ResourcesCompat
//import com.google.mlkit.vision.face.Face
//import com.liveness.sdk.core.api.HttpClientUtils
//import com.liveness.sdk.core.facedetector.FaceDetector
//import com.liveness.sdk.core.facedetector.Frame
//import com.liveness.sdk.core.facedetector.LensFacing
//import com.liveness.sdk.core.model.LivenessModel
//import com.liveness.sdk.core.utils.AppConfig
//import com.liveness.sdk.core.utils.AppPreferenceUtils
//import com.liveness.sdk.core.utils.AppUtils
//import com.liveness.sdk.core.utils.TotpUtils
//import com.nimbusds.jose.shaded.gson.Gson
//import com.otaliastudios.cameraview.CameraException
//import com.otaliastudios.cameraview.CameraListener
//import com.otaliastudios.cameraview.CameraOptions
//import com.otaliastudios.cameraview.CameraView
//import com.otaliastudios.cameraview.PictureResult
//import com.otaliastudios.cameraview.VideoResult
//import com.otaliastudios.cameraview.controls.Facing
//import com.otaliastudios.cameraview.controls.Mode
//import org.json.JSONObject
//import java.util.Random
//import java.util.UUID
//
//
///**
// * Created by Thuytv on 15/04/2024.
// */
//internal class MainLiveNessActivity_bk : Activity() {
//    private val REQUEST_PERMISSION_CODE = 1231
//
//    //    private var pathVideo = ""
//    private var bgColor = 0
//    private var isCapture = false
//    private var lstBgDefault: ArrayList<Int> = arrayListOf(R.drawable.img_0, R.drawable.img_1, R.drawable.img_2, R.drawable.img_3)
//
//    private var isFirstVideo = true
//    private var typeScreen = ""
//    private lateinit var cameraViewVideo: CameraView
//    private lateinit var btnCapture: Button
//    private lateinit var prbLoading: ProgressBar
//    private lateinit var bgFullScreenDefault: ImageView
//    private lateinit var llVideo: RelativeLayout
//    private var mImgLiveNess: String = ""
//    private var mFaceImage: String = ""
//
//    private var permissions = arrayOf(
//        Manifest.permission.CAMERA
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.ui_main_live_ness)
//
//        if (AppConfig.mCustomView != null) {
//            findViewById<FrameLayout>(R.id.frame_view_custom).addView(AppConfig.mCustomView)
//        }
//        cameraViewVideo = findViewById(R.id.camera_view_video)
//        btnCapture = findViewById(R.id.btn_capture)
//        prbLoading = findViewById(R.id.prb_loading)
//        bgFullScreenDefault = findViewById(R.id.bg_full_screen_default)
//        llVideo = findViewById(R.id.ll_video)
//
//        typeScreen = intent.getStringExtra(AppConfig.KEY_BUNDLE_SCREEN) ?: ""
//        findViewById<ImageView>(R.id.imv_back).setOnClickListener {
//            finish()
//        }
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//            permissions = arrayOf(
//                Manifest.permission.CAMERA,
//                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
//            )
//        }
//
//        initCamera()
//        if (checkPermissions()) {
//            cameraViewVideo.open()
//        } else {
//            requestPermissions()
//        }
//        if (typeScreen == AppConfig.TYPE_SCREEN_REGISTER_FACE) {
//            btnCapture.setOnClickListener {
//                cameraViewVideo.takePictureSnapshot()
//            }
//        } else {
//            btnCapture.visibility = View.GONE
//        }
//        AppConfig.mActionView?.setOnClickListener {
//            cameraViewVideo.takePictureSnapshot()
//        }
//    }
//
//    private fun checkPermissions(): Boolean {
//        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//            val resultCamera = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
//            val resultRead = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
//            val resultWrite = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            (resultCamera == PackageManager.PERMISSION_GRANTED
//                    && resultRead == PackageManager.PERMISSION_GRANTED
//                    && resultWrite == PackageManager.PERMISSION_GRANTED)
//        } else {
//            val resultCamera = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
//            resultCamera == PackageManager.PERMISSION_GRANTED
//        }
//    }
//
//    private fun requestPermissions() {
//        ActivityCompat.requestPermissions(this@MainLiveNessActivity_bk, permissions, REQUEST_PERMISSION_CODE)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == REQUEST_PERMISSION_CODE) {
//            if (checkPermissions()) {
////                Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG).show()
//                cameraViewVideo.open()
//            } else {
//                Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG).show()
//            }
////            if (grantResults.isNotEmpty()) {
////                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                    Toast.makeText(applicationContext, "Permission granted", Toast.LENGTH_LONG).show()
////                    init()
////                } else {
////                    Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_LONG).show()
////                }
////            }
//        }
//    }
//
//    private fun initCamera() {
//        apply {
////            pathVideo = Environment.getExternalStorageDirectory().toString() + "/Download/" + "VideoLiveNess" + System.currentTimeMillis() + ".mp4"
//            val lensFacing = Facing.FRONT
//            setupCamera(lensFacing)
//        }
//    }
//
//    private fun setupCamera(lensFacing: Facing) = apply {
//        val faceDetector = FaceDetector(findViewById(R.id.faceBoundsOverlay))
//        faceDetector.setonFaceDetectionFailureListener(object : FaceDetector.OnFaceDetectionResultListener {
//            override fun onSuccess(faceBounds: Face) {
//                super.onSuccess(faceBounds)
//                if (!isCapture) {
//                    isCapture = true
////                    cameraViewVideo.stopVideo()
//                    bgFullScreenDefault.visibility = View.VISIBLE
//                    llVideo.visibility = View.GONE
//                    bgColor = Random().nextInt(3)
//                    bgFullScreenDefault.background = ResourcesCompat.getDrawable(resources, lstBgDefault[bgColor], this@MainLiveNessActivity_bk.theme)
//                    Handler(Looper.myLooper()!!).postDelayed({
//                        cameraViewVideo.takePictureSnapshot()
//                    }, 100)
//                }
//            }
//
//        })
//        cameraViewVideo.facing = lensFacing
//        cameraViewVideo.mode = Mode.PICTURE
//
//
//        cameraViewVideo.addCameraListener(object : CameraListener() {
//            override fun onCameraOpened(options: CameraOptions) {
//                super.onCameraOpened(options)
//                if (typeScreen != AppConfig.TYPE_SCREEN_REGISTER_FACE) {
////                    cameraViewVideo.takeVideoSnapshot(File(pathVideo))
//                } else {
//                    if (AppConfig.mCustomView == null) {
//                        btnCapture.visibility = View.VISIBLE
//                    } else {
//                        btnCapture.visibility = View.GONE
//                    }
//                }
//            }
//
//            override fun onCameraError(exception: CameraException) {
//                super.onCameraError(exception)
//                AppUtils.showLog("Thuytv--onCameraError")
//            }
//
//            override fun onCameraClosed() {
//                super.onCameraClosed()
//                AppUtils.showLog("Thuytv--onCameraClosed")
//            }
//
//            override fun onPictureTaken(result: PictureResult) {
//                super.onPictureTaken(result)
//                if (typeScreen != AppConfig.TYPE_SCREEN_REGISTER_FACE) {
//                    result.data.let {
//                        mImgLiveNess = android.util.Base64.encodeToString(it, android.util.Base64.NO_PADDING)
//                        callAPIGEtTOTP(mImgLiveNess, bgColor)
//                    }
//                    bgFullScreenDefault.visibility = View.GONE
//                    llVideo.visibility = View.VISIBLE
//                    isCapture = false
//                } else {
//                    result.data.let {
//                        mFaceImage = android.util.Base64.encodeToString(it, android.util.Base64.NO_PADDING)
//                        registerFace(mFaceImage)
//                    }
//                }
//            }
//
//            override fun onVideoTaken(result: VideoResult) {
//                super.onVideoTaken(result)
//                result.file.let {
////                    AppConfig.livenessListener?.onCallbackLiveness(LivenessModel(pathVideo = it.absolutePath))
////                    finish()
//                }
//            }
//        })
//        if (typeScreen != AppConfig.TYPE_SCREEN_REGISTER_FACE) {
//            cameraViewVideo.addFrameProcessor {
//                faceDetector.process(
//                    Frame(
//                        data = it.getData(),
//                        rotation = it.rotationToUser,
//                        size = Size(it.size.width, it.size.height),
//                        format = it.format,
//                        lensFacing = if (cameraViewVideo.facing == Facing.BACK) LensFacing.BACK else LensFacing.FRONT
//                    )
//                )
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        cameraViewVideo.open()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        cameraViewVideo.close()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        cameraViewVideo.destroy()
//        if (AppConfig.mCustomView != null) {
//            findViewById<FrameLayout>(R.id.frame_view_custom).removeView(AppConfig.mCustomView)
//        }
//    }
//
//    private fun initTransaction(tOTP: String, imgLiveNess: String, bgColor: Int) {
//        val response = HttpClientUtils.instance?.initTransaction(this)
//        var result: JSONObject? = null
//        if (!response.isNullOrEmpty()) {
//            result = JSONObject(response)
//        }
//        var status = -1
//        if (result?.has("status") == true) {
//            status = result.getInt("status")
//        }
//        var strMessage = "Error"
//        if (result?.has("message") == true) {
//            strMessage = result.getString("message")
//        }
//        if (status == 200) {
//            val transactionId = result?.getString("data") ?: ""
////            val signature = result?.getString("signature")
//            checkLiveNessFlash(tOTP, transactionId, imgLiveNess, bgColor)
//        } else {
//            showToast(strMessage)
//        }
//    }
//
//    private fun checkLiveNessFlash(tOTP: String, transactionID: String, imgLiveNess: String, bgColor: Int) {
//        val response = HttpClientUtils.instance?.checkLiveNessFlash(this, tOTP, transactionID, imgLiveNess, bgColor)
//        var result: JSONObject? = null
//        if (response?.isNotEmpty() == true) {
//            result = JSONObject(response)
//        }
//        var status = -1
//        if (result?.has("status") == true) {
//            status = result.getInt("status")
//        }
//        var strMessage = "Error"
//        if (result?.has("message") == true) {
//            strMessage = result.getString("message")
//        }
//        if (status == 200) {
//            val liveNessModel = Gson().fromJson<LivenessModel>(response, LivenessModel::class.java)
////            if (liveNessModel.success == true) {
////            liveNessModel.pathVideo = pathVideo
//            liveNessModel.livenessImage = mImgLiveNess
//            this.runOnUiThread {
//                showLoading(false)
////                AppUtils.showLog("Thuytv------pathVideo: $pathVideo")
//                AppConfig.livenessListener?.onCallbackLiveness(liveNessModel)
//                finish()
//            }
////            } else {
////                showToast(result?.getString("message") ?: "Error")
////            }
//        } else {
////            showToast(result?.getString("message") ?: "Error")
//            this.runOnUiThread {
//                showLoading(false)
//                AppConfig.livenessListener?.onCallbackLiveness(LivenessModel(status = status, message = strMessage))
//                finish()
//            }
//        }
//    }
//
//    fun callAPIGEtTOTP(imgLiveNess: String, bgColor: Int) {
//        showLoading(true)
//        Thread {
//            val tOTP = TotpUtils(this).getTotp()
//            if (tOTP.isNullOrEmpty() || tOTP == "-1") {
//                showToast("TOTP null")
//            } else {
//                initTransaction(tOTP, imgLiveNess, bgColor)
//            }
//        }.start()
//    }
//
//    private fun showToast(strToast: String) {
//        this.runOnUiThread {
//            Toast.makeText(this, strToast, Toast.LENGTH_SHORT).show()
//            showLoading(false)
//        }
//    }
//
//    private fun showLoading(isShow: Boolean) {
//        this.runOnUiThread {
//            if (isShow) {
//                if (AppConfig.mProgressView == null) {
//                    prbLoading.visibility = View.VISIBLE
//                } else {
//                    AppConfig.mProgressView?.visibility = View.VISIBLE
//                }
//            } else {
//                if (AppConfig.mProgressView == null) {
//                    prbLoading.visibility = View.GONE
//                } else {
//                    AppConfig.mProgressView?.visibility = View.GONE
//                }
//            }
//        }
//
//    }
//
//    private fun registerFace(faceImage: String) {
//        showLoading(true)
//        Thread {
//            var mSecret = AppPreferenceUtils(this).getTOTPSecret(this) ?: AppConfig.mLivenessRequest?.secret
//            if (mSecret.isNullOrEmpty() || mSecret.length != 16) {
//                mSecret = AppUtils.getSecretValue()
//            }
//            var mDeviceId = AppPreferenceUtils(this).getDeviceId() ?: AppConfig.mLivenessRequest?.deviceId
//            if (mDeviceId.isNullOrEmpty()) {
//                mDeviceId = UUID.randomUUID().toString()
//            }
//            val request = JSONObject()
//            request.put("deviceId", mDeviceId)
//            request.put("deviceOs", "Android")
//            request.put("deviceName", Build.MANUFACTURER + " " + Build.MODEL)
//            request.put("period", AppConfig.mLivenessRequest?.duration)
//            request.put("secret", mSecret)
//            val responseDevice = HttpClientUtils.instance?.postV3("/eid/v3/registerDevice", request)
//            var result: JSONObject? = null
//            if (responseDevice != null && responseDevice.length > 0) {
//                result = JSONObject(responseDevice)
//            }
//            var statusDevice = -1
//            if (result?.has("status") == true) {
//                statusDevice = result.getInt("status")
//            }
//            var strMessageDevice = "Error"
//            if (result?.has("message") == true) {
//                strMessageDevice = result.getString("message")
//            }
//            if (statusDevice == 200) {
//                AppPreferenceUtils(this).setDeviceId(mDeviceId)
//                AppPreferenceUtils(this).setTOTPSecret(this, mSecret)
//                val response = HttpClientUtils.instance?.registerFace(this, faceImage)
//                var result: JSONObject? = null
//                if (response?.isNotEmpty() == true) {
//                    result = JSONObject(response)
//                }
//                var status = -1
//                if (result?.has("status") == true) {
//                    status = result.getInt("status")
//                }
//                var strMessage = "Error"
//                if (result?.has("message") == true) {
//                    strMessage = result.getString("message")
//                }
//                if (status == 200) {
//                    showLoading(false)
//
//                    this.runOnUiThread {
//                        AppConfig.livenessFaceListener?.onCallbackLiveness(LivenessModel(faceImage = mFaceImage))
//                        showToast("Register Face Success")
//                        AppPreferenceUtils(this).setRegisterFace(true)
//                        finish()
//                    }
//
//                } else {
//                    showLoading(false)
////                    showToast(result?.getString("message") ?: "Error")
//                    this.runOnUiThread {
//                        AppConfig.livenessFaceListener?.onCallbackLiveness(LivenessModel(status = status, message = strMessage))
//                        finish()
//                    }
//                }
//            } else {
//                this.runOnUiThread {
//                    AppConfig.livenessFaceListener?.onCallbackLiveness(LivenessModel(status = statusDevice, message = strMessageDevice))
//                    finish()
//                }
//            }
//        }.start()
//
//    }
//
//}