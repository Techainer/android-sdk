package io.liveness.flash.core.facedetector

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.RectF
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.GuardedBy
import com.google.android.gms.common.util.concurrent.HandlerExecutor
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class FaceDetectorBio(
    private val faceBoundsOverlay: FaceBoundsOverlayBio,
    private val mMinFaceSize: Float = 0.15F
) {

    private val mlKitFaceDetector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(mMinFaceSize)
            .enableTracking()
            .build()
    )

    /** Listener that gets notified when a face detection result is ready. */
    private var onFaceDetectionResultListener: OnFaceDetectionResultListener? = null

    /** [Executor] used to run the face detection on a background thread.  */
    private lateinit var faceDetectionExecutor: ExecutorService

    /** [Executor] used to trigger the rendering of the detected face bounds on the UI thread. */
    private val mainExecutor = HandlerExecutor(Looper.getMainLooper())

    /** Controls access to [isProcessing], since it can be accessed from different threads. */
    private val lock = Object()

    @GuardedBy("lock")
    private var isProcessing = false

    init {
        Log.d("mMinFaceSize", "mMinFaceSize=$mMinFaceSize")
        faceBoundsOverlay.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                faceDetectionExecutor = Executors.newSingleThreadExecutor()
            }

            override fun onViewDetachedFromWindow(view: View) {
                if (::faceDetectionExecutor.isInitialized) {
                    faceDetectionExecutor.shutdown()
                }
            }
        })
    }

    /** Sets a listener to receive face detection result callbacks. */
    fun setonFaceDetectionFailureListener(listener: OnFaceDetectionResultListener) {
        onFaceDetectionResultListener = listener
    }

    /**
     * Kick-starts a face detection operation on a camera frame. If a previous face detection
     * operation is still ongoing, the frame is dropped until the face detector is no longer busy.
     */
    fun process(frame: Frame) {
        synchronized(lock) {
            if (!isProcessing) {
                isProcessing = true
                if (!::faceDetectionExecutor.isInitialized) {
                    val exception =
                        IllegalStateException(
                            "Cannot run face detection. Make sure the face " +
                                    "bounds overlay is attached to the current window."
                        )
                    onError(exception)
                } else {
                    faceDetectionExecutor.execute { frame.detectFaces() }
                }
            }
        }
    }
    fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }
    private fun Frame.detectFaces() {
        val dataImage = data ?: return
        val inputImage = InputImage.fromByteArray(dataImage, size.width, size.height, rotation, format)
        mlKitFaceDetector.process(inputImage)
            .addOnSuccessListener { faces ->
                synchronized(lock) {
                    isProcessing = false
                }

                // done: 8, start: 9, detect: 0, check: 2

                // Correct the detected faces so that they're correctly rendered on the UI, then
                // pass them to [faceBoundsOverlay] to be drawn.
                if(faces.size > 0) {
                    onFaceDetectionResultListener?.onStartCheckSmile()
                    for (face in faces) {
                        Log.d("FACE", "FACE === $face")
                        if (face.smilingProbability != null) {
                            val smile = face.smilingProbability ?: 0.0f
                            if (smile > 0.95) {
                                onFaceDetectionResultListener?.onSuccess(face)
                                isProcessing = true
                            } else {
                                val faceBounds = faces.map { face -> face.toFaceBounds(this) }
//                                mainExecutor.execute { faceBoundsOverlay.updateFaces(faceBounds) }
                                onFaceDetectionResultListener?.onDetectingFace(face)
                            }
                        } else
                            onFaceDetectionResultListener?.onDetectingFace(face)
                    }
                }else {
//                    val faceBounds = faces.map { face -> face.toFaceBounds(this) }
//                    mainExecutor.execute { faceBoundsOverlay.updateFaces(faceBounds) }
                }
            }
            .addOnFailureListener { exception ->
                synchronized(lock) {
                    isProcessing = false
                }
                onError(exception)
            }
    }

    /**
     * Converts a [Face] to an instance of [FaceBoundsBio] while correctly transforming the face's
     * bounding box by scaling it to match the overlay and mirroring it when the lens facing
     * represents the front facing camera.
     */
    private fun Face.toFaceBounds(frame: Frame): FaceBoundsBio {
        // In order to correctly display the face bounds, the orientation of the processed image
        // (frame) and that of the overlay have to match. Which is why the dimensions of
        // the analyzed image are reversed if its rotation is 90 or 270.
        val reverseDimens = frame.rotation == 90 || frame.rotation == 270
        val width = if (reverseDimens) frame.size.height else frame.size.width
        val height = if (reverseDimens) frame.size.width else frame.size.height

        // Since the analyzed image (frame) probably has a different resolution (width and height)
        // compared to the overlay view, we compute by how much we have to scale the bounding box
        // so that it is displayed correctly on the overlay.
        val scaleX = faceBoundsOverlay.width.toFloat() / width
        val scaleY = faceBoundsOverlay.height.toFloat() / height

        // If the front camera lens is being used, reverse the right/left coordinates
        val isFrontLens = frame.lensFacing == LensFacingBio.FRONT
        val flippedLeft = if (isFrontLens) width - boundingBox.right else boundingBox.left
        val flippedRight = if (isFrontLens) width - boundingBox.left else boundingBox.right

        // Scale all coordinates to match the overlay
        val scaledLeft = scaleX * flippedLeft
        val scaledTop = scaleY * boundingBox.top
        val scaledRight = scaleX * flippedRight
        val scaledBottom = scaleY * boundingBox.bottom
        val scaledBoundingBox = RectF(scaledLeft, scaledTop, scaledRight, scaledBottom)

        // Return the scaled bounding box and a tracking id of the detected face. The tracking id
        // remains the same as long as the same face continues to be detected.
        return FaceBoundsBio(
            trackingId,
            scaledBoundingBox
        )
    }

    private fun onError(exception: Exception) {
        onFaceDetectionResultListener?.onFailure(exception)
        Log.e(TAG, "An error occurred while running a face detection", exception)
    }

    /**
     * Interface containing callbacks that are invoked when the face detection process succeeds or
     * fails.
     */
    interface OnFaceDetectionResultListener {
        /**
         * Signals that the face detection process has successfully completed for a camera frame.
         * It also provides the result of the face detection for further potential processing.
         *
         * @param faceBounds Detected faces from a camera frame
         */
        fun onSuccess(faceBounds: Face) {}

        fun onDetectingFace(faceBounds: Face) {}
        fun onStartCheckSmile() {}

        /**
         * Invoked when an error is encountered while attempting to detect faces in a camera frame.
         *
         * @param exception Encountered [Exception] while attempting to detect faces in a camera
         * frame.
         */
        fun onFailure(exception: Exception) {}
    }

    companion object {
        private const val TAG = "FaceDetector"
    }
}