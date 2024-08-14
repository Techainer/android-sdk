package com.liveness.sdk.core.facedetector

import android.util.Size

internal data class Frame(
    @Suppress("ArrayInDataClass") val data: ByteArray?,
    val rotation: Int,
    val size: Size,
    val format: Int,
    val lensFacing: LensFacingBio
)