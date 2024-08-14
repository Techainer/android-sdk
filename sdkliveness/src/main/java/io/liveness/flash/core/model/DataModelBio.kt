package com.liveness.sdk.core.model

import androidx.annotation.Keep
import com.nimbusds.jose.shaded.gson.annotations.SerializedName

/**
 * Created by Thuytv on 16/04/2024.
 */
@Keep
data class DataModelBio(
    @field:SerializedName("faceMatchingScore")
    var faceMatchingScore: String? = null,
    @field:SerializedName("livenessType")
    var livenessType: String? = null,
    @field:SerializedName("livenesScore")
    var livenesScore: Float? = null
)