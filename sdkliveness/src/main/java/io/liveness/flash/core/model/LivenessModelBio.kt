package io.liveness.flash.core.model

import androidx.annotation.Keep
import com.nimbusds.jose.shaded.gson.annotations.SerializedName

/**
 * Created by Thuytv on 16/04/2024.
 */
@Keep
data class LivenessModelBio(
    @field:SerializedName("status")
    var status: Int? = null,
    @field:SerializedName("message")
    var message: String? = null,
    @field:SerializedName("request_id")
    var requestId: String? = null,
    @field:SerializedName("data")
    var data: DataModelBio? = null,
    @field:SerializedName("code")
    var code: String? = null,
    @field:SerializedName("signature")
    var signature: String? = null,
    @field:SerializedName("success")
    var success: Boolean? = null,
    @field:SerializedName("path_video")
    var pathVideo: String? = null,
    @field:SerializedName("face_image")
    var faceImage: String? = null,
    @field:SerializedName("liveness_image")
    var livenessImage: String? = null,
    @field:SerializedName("transaction_id")
    var transactionID: String? = null,
    @field:SerializedName("action")
    var action: Int? = null,
    @field:SerializedName("bgColor")
    var bgColor: Int? = null
)