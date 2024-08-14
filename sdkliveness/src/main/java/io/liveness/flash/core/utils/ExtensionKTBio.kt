package com.liveness.sdk.core.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.result.ActivityResultLauncher
import java.io.ByteArrayOutputStream

/**
 * Created by Thuytv on 15/04/2024.
 */
class extensionKT {
    fun Activity.requestPermissions(request: ActivityResultLauncher<Array<String>>, permissions: Array<String>) = request.launch(permissions)

//    fun Activity.isAllPermissionsGranted(permissions: Array<String>) = permissions.all {
//        ContextCompat.checkSelfPermission(requireContext, it) == PackageManager.PERMISSION_GRANTED
//    }

    fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }
}