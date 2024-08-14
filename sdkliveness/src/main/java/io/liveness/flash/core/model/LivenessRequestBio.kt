package io.liveness.flash.core.model

import androidx.annotation.Keep
import com.nimbusds.jose.shaded.gson.annotations.SerializedName

/**
 * Created by Thuytv on 16/04/2024.
 */
@Keep
data class LivenessRequestBio(
    @field:SerializedName("duration")
    var duration: Int? = null,
    @field:SerializedName("device_id")
    var deviceId: String? = null,
    @field:SerializedName("private_key")
    var privateKey: String? = null,
    @field:SerializedName("app_id")
    var appId: String? = null,
    @field:SerializedName("client_transaction_id")
    var clientTransactionId: String? = null,
    @field:SerializedName("owner_id")
    var ownerId: String? = null,
    @field:SerializedName("secret")
    var secret: String? = null,
    @field:SerializedName("base_url")
    var baseURL: String? = null,
    @field:SerializedName("public_key")
    var publicKey: String? = null,
    @field:SerializedName("image_face")
    var imageFace: String? = null,
    @field:SerializedName("option_header")
    var optionHeader: HashMap<String, String>? = null,
    @field:SerializedName("option_request")
    var optionRequest: HashMap<String, String>? = null,
    @field:SerializedName("is_debug")
    var isDebug: Boolean? = null,
    @field:SerializedName("is_video")
    var isVideo: Boolean = false,
    @field:SerializedName("mMinFaceSize")
    var mMinFaceSize: Float = 0.15F
)