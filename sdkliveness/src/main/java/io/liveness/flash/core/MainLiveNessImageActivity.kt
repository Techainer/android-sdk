package com.liveness.sdk.core

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.mlkit.vision.face.Face
import com.liveness.sdk.core.api.HttpClientUtilsBio
import com.liveness.sdk.core.facedetector.FaceDetectorBio
import com.liveness.sdk.core.facedetector.Frame
import com.liveness.sdk.core.facedetector.LensFacingBio
import com.liveness.sdk.core.model.LivenessModelBio
import com.liveness.sdk.core.utils.AppConfigBio
import com.liveness.sdk.core.utils.AppPreferenceUtilsBio
import com.liveness.sdk.core.utils.AppUtilsBio
import com.liveness.sdk.core.utils.TotpUtilsBio
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

internal class MainLiveNessImageActivity : Fragment() {
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
    private var frameViewCustom: FrameLayout? = null
    private var mImgLiveNess: String = ""
    private var mFaceImage: String = ""
    private var mFragmentManager: FragmentManager? = null
    private var mViewRoot: View? = null

    private var permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.activity_main_live_ness_image, container, false)
        this.mViewRoot = view
        cameraViewVideo = view.findViewById(R.id.camera_view_video)
        btnCapture = view.findViewById(R.id.btnCapture)
        prbLoading = view.findViewById(R.id.prbLoading)
        bgFullScreenDefault = view.findViewById(R.id.bgFullScreenDefault)
        llVideo = view.findViewById(R.id.llVideo)
        frameViewCustom = view.findViewById(R.id.frameViewCustom)
        if (AppConfigBio.mCustomView != null) {
            frameViewCustom?.addView(AppConfigBio.mCustomView)
        }
        typeScreen = arguments?.getString(AppConfigBio.KEY_BUNDLE_SCREEN) ?: ""
        view.findViewById<ImageView>(R.id.imvBack).setOnClickListener {
            activity?.finish()
        }
        initCamera(view)
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
            cameraViewVideo.takePictureSnapshot()
        }
        return view
    }

    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            val resultCamera = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            val resultRead = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            val resultWrite = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            (resultCamera == PackageManager.PERMISSION_GRANTED
                    && resultRead == PackageManager.PERMISSION_GRANTED
                    && resultWrite == PackageManager.PERMISSION_GRANTED)
        } else {
            val resultCamera = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            resultCamera == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), permissions, REQUEST_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (checkPermissions()) {
//                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_LONG).show()
                cameraViewVideo.open()
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
            }
//            if (grantResults.isNotEmpty()) {
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_LONG).show()
//                    init()
//                } else {
//                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_LONG).show()
//                }
//            }
        }
    }

    private fun initCamera(view: View) {
        apply {
//            pathVideo = Environment.getExternalStorageDirectory().toString() + "/Download/" + "VideoLiveNess" + System.currentTimeMillis() + ".mp4"
            val folderVideo = File(requireActivity().cacheDir, "VideoLiveNess")
            if (folderVideo.exists()) {
                folderVideo.deleteRecursively()
            }
            folderVideo.mkdir()
            folderVideo.createNewFile()
            val fileMp4 = File(folderVideo.parentFile , "Video${System.currentTimeMillis()}.mp4")
            pathVideo = fileMp4.absolutePath
            val lensFacing = Facing.FRONT
            setupCamera(lensFacing, view)
        }
    }

    private fun setupCamera(lensFacing: Facing, view: View) = apply {
        val faceDetector = FaceDetectorBio(view.findViewById(R.id.faceBoundsOverlayImage), AppConfigBio.mLivenessRequest?.mMinFaceSize ?: 0.15F)
        faceDetector.setonFaceDetectionFailureListener(object : FaceDetectorBio.OnFaceDetectionResultListener {
            override fun onSuccess(faceBounds: Face) {
                super.onSuccess(faceBounds)
                if (!isCapture) {
                    AppConfigBio.livenessListener?.onCallbackLiveness(
                        LivenessModelBio(
                            message = "check smile",
                            action = 2
                        )
                    )
                    isCapture = true
//                    cameraViewVideo.stopVideo()
                    bgFullScreenDefault.visibility = View.VISIBLE
                    llVideo.visibility = View.GONE
                    bgColor = Random().nextInt(3)
                    bgFullScreenDefault.background = ResourcesCompat.getDrawable(resources, lstBgDefault[bgColor], requireContext().theme)
                    Handler(Looper.myLooper()!!).postDelayed({
                        cameraViewVideo.takePictureSnapshot()
                    }, 100)
                }
            }
            // done: 8, start: 9, detect: 0, check: 2

            override fun onStartCheckSmile() {
                super.onStartCheckSmile()
                AppConfigBio.livenessListener?.onCallbackLiveness(
                    LivenessModelBio(
                        message = "start check smile",
                        action = 9
                    )
                )
            }

            override fun onDetectingFace(faceBounds: Face) {
                super.onDetectingFace(faceBounds)
                AppConfigBio.livenessListener?.onCallbackLiveness(
                    LivenessModelBio(
                        message = "detect face",
                        action = 0
                    )
                )
            }

        })
        cameraViewVideo.facing = lensFacing
        cameraViewVideo.mode = Mode.PICTURE


        cameraViewVideo.addCameraListener(object : CameraListener() {
            override fun onCameraOpened(options: CameraOptions) {
                super.onCameraOpened(options)
//                val folderVideo = File(requireActivity().cacheDir, "VideoLiveNess")
//                if (folderVideo.exists()) {
//                    folderVideo.deleteRecursively()
//                }
//                folderVideo.mkdir()
//                folderVideo.createNewFile()
//                val fileMp4 = File(folderVideo.parentFile , "Video${System.currentTimeMillis()}.mp4")

                cameraViewVideo.takeVideoSnapshot(File(pathVideo))
                if (typeScreen != AppConfigBio.TYPE_SCREEN_REGISTER_FACE) {
//                    cameraViewVideo.takeVideoSnapshot(File(pathVideo))
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
                cameraViewVideo.close()
                cameraViewVideo.open()
            }

            override fun onCameraClosed() {
                super.onCameraClosed()
//                if (mImgLiveNess.isNotEmpty() && pathVideo.isNotEmpty()) {
//                    callBackNativeEvent(mImgLiveNess)
//                } else {
//                    Toast.makeText(requireContext(), "ImgLiveNess null", Toast.LENGTH_LONG).show()
//                }
            }

            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                if (typeScreen != AppConfigBio.TYPE_SCREEN_REGISTER_FACE) {
                    result.data.let {
                        mImgLiveNess = android.util.Base64.encodeToString(it, android.util.Base64.NO_PADDING)
//                        callAPIGEtTOTP(mImgLiveNess, bgColor)
//                        checkLiveNess(mImgLiveNess, bgColor)
                        // res image
                        cameraViewVideo.stopVideo()
                    }
                    bgFullScreenDefault.visibility = View.GONE
                    llVideo.visibility = View.VISIBLE
                    isCapture = false

                    if (mImgLiveNess.isNotEmpty() && pathVideo.isNotEmpty()) {
                        callBackNativeEvent(mImgLiveNess)
//                        onBackFragment()
                    }
                } else {
                    result.data.let {
                        mFaceImage = android.util.Base64.encodeToString(it, android.util.Base64.NO_PADDING)
                        registerFace(mFaceImage)
                    }
                }
            }

            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)
                if (result?.file != null) {
                    pathVideo = result?.file.absolutePath
                } else {
                    pathVideo = ""
                }
                cameraViewVideo.close()
                if (mImgLiveNess.isNotEmpty() && pathVideo.isNotEmpty()) {
                    callBackNativeEvent(mImgLiveNess)
//                    onBackFragment()
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
        if (cameraViewVideo != null) {
            cameraViewVideo.open()
        }
    }

    override fun onPause() {
        super.onPause()
        if (cameraViewVideo != null) {
            cameraViewVideo.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraViewVideo != null) {
            cameraViewVideo.close()
            cameraViewVideo.destroy()
        }
        if (AppConfigBio.mCustomView != null) {
            frameViewCustom?.removeView(AppConfigBio.mCustomView)
            AppConfigBio.mCustomView = null
        }
        if (AppConfigBio.mProgressView != null) {
            frameViewCustom?.removeView(AppConfigBio.mProgressView)
            AppConfigBio.mProgressView = null
        }
    }

    private fun initTransaction(tOTP: String, imgLiveNess: String, bgColor: Int) {
        val response = HttpClientUtilsBio.instance?.initTransaction(requireContext())
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
        val response = HttpClientUtilsBio.instance?.checkLiveNessFlash(requireContext(), tOTP, transactionID, imgLiveNess, bgColor)
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
            val liveNessModel = Gson().fromJson<LivenessModelBio>(response, LivenessModelBio::class.java)
//            if (liveNessModel.success == true) {
//            liveNessModel.pathVideo = pathVideo
            liveNessModel.livenessImage = mImgLiveNess
            liveNessModel.transactionID = transactionID
            activity?.runOnUiThread {
                showLoading(false)
                AppConfigBio.livenessListener?.onCallbackLiveness(liveNessModel)
                onBackFragment()
            }
//            } else {
//                showToast(result?.getString("message") ?: "Error")
//            }
        } else {
            activity?.runOnUiThread {
                showLoading(false)
                AppConfigBio.livenessListener?.onCallbackLiveness(LivenessModelBio(status = status, message = strMessage))
                onBackFragment()
            }
        }
    }

    private fun onBackFragment() {
        mFragmentManager?.popBackStack()
    }

    fun setFragmentManager(fragmentManager: FragmentManager) {
        mFragmentManager = fragmentManager
    }

    fun callBackNativeEvent(image: String) {
        Thread {
            AppConfigBio.livenessListener?.onCallbackLiveness(
                LivenessModelBio(
                    message = "done smile",
                    action = 8,
                    livenessImage = image,
                    pathVideo = pathVideo
                ))
        }.start()
    }

    fun checkLiveNess(imgLiveNess: String, bgColor: Int) {
        showLoading(true)
        Thread {
            val tOTP = TotpUtilsBio(requireContext()).getTotp()
            if (tOTP.isNullOrEmpty() || tOTP == "-1") {
                showLoading(false)
                AppConfigBio.livenessListener?.onCallbackLiveness(
                    LivenessModelBio(
                        status = 101,
                        message = "TOTP null"
                    ))
            } else {
                val response = HttpClientUtilsBio.instance?.checkLiveNess(requireContext(), tOTP, imgLiveNess, bgColor)
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
                    val liveNessModel = Gson().fromJson<LivenessModelBio>(response, LivenessModelBio::class.java)
                    liveNessModel.livenessImage = mImgLiveNess
                    activity?.runOnUiThread {
                        showLoading(false)
                        AppConfigBio.livenessListener?.onCallbackLiveness(liveNessModel)
                        cameraViewVideo.close()
                        AppConfigBio.livenessListener?.onCallbackLiveness(
                            LivenessModelBio(
                                message = "done smile",
                                action = 8
                            )
                        )
                    }
                } else {
                    activity?.runOnUiThread {
                        showLoading(false)
                        AppConfigBio.livenessListener?.onCallbackLiveness(LivenessModelBio(status = status, message = strMessage))
                        cameraViewVideo.close()
                    }
                }
            }
        }.start()
    }

    fun callAPIGEtTOTP(imgLiveNess: String, bgColor: Int) {
        showLoading(true)
        Thread {
            val tOTP = TotpUtilsBio(requireContext()).getTotp()
            if (tOTP.isNullOrEmpty() || tOTP == "-1") {
                showToast("TOTP null")
            } else {
                initTransaction(tOTP, imgLiveNess, bgColor)
            }
        }.start()
    }

    private fun showToast(strToast: String) {
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), strToast, Toast.LENGTH_SHORT).show()
            showLoading(false)
        }
    }

    private fun showLoading(isShow: Boolean) {
        activity?.runOnUiThread {
            if (isShow) {
                if (AppConfigBio.mProgressView == null) {
                    prbLoading.visibility = View.VISIBLE
                } else {
//                    AppConfig.mProgressView?.visibility = View.VISIBLE
                    frameViewCustom?.addView(AppConfigBio.mProgressView)
                }
            } else {
                if (AppConfigBio.mProgressView == null) {
                    prbLoading.visibility = View.GONE
                } else {
                    frameViewCustom?.removeView(AppConfigBio.mProgressView)
                    AppConfigBio.mProgressView = null
//                    AppConfig.mProgressView?.visibility = View.GONE
                }
            }
        }

    }

    private fun registerFace(faceImage: String) {
        showLoading(true)
        Thread {
            var mSecret = AppPreferenceUtilsBio(requireContext()).getTOTPSecret(requireContext()) ?: AppConfigBio.mLivenessRequest?.secret
            if (mSecret.isNullOrEmpty() || mSecret.length != 16) {
                mSecret = AppUtilsBio.getSecretValue()
            }
            var mDeviceId = AppPreferenceUtilsBio(requireContext()).getDeviceId() ?: AppConfigBio.mLivenessRequest?.deviceId
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
                AppPreferenceUtilsBio(requireContext()).setDeviceId(mDeviceId)
                AppPreferenceUtilsBio(requireContext()).setTOTPSecret(requireContext(), mSecret)
                val response = HttpClientUtilsBio.instance?.registerFace(requireContext(), faceImage)
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

                    activity?.runOnUiThread {
                        AppConfigBio.livenessFaceListener?.onCallbackLiveness(LivenessModelBio(faceImage = mFaceImage))
//                        showToast("Register Face Success")
                        AppPreferenceUtilsBio(requireContext()).setRegisterFace(true)
                        onBackFragment()
                    }

                } else {
                    showLoading(false)
//                    showToast(result?.getString("message") ?: "Error")
                    activity?.runOnUiThread {
                        AppConfigBio.livenessFaceListener?.onCallbackLiveness(LivenessModelBio(status = status, message = strMessage))
                        onBackFragment()
                    }
                }
            } else {
                activity?.runOnUiThread {
                    AppConfigBio.livenessFaceListener?.onCallbackLiveness(LivenessModelBio(status = statusDevice, message = strMessageDevice))
                    onBackFragment()
                }
            }
        }.start()

    }

}