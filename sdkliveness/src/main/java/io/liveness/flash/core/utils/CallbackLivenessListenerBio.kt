package com.liveness.sdk.core.utils

import androidx.annotation.Keep
import com.liveness.sdk.core.model.LivenessModelBio

/**
 * Created by Thuytv on 16/04/2024.
 */
@Keep
interface CallbackLivenessListenerBio {
    fun onCallbackLiveness(data: LivenessModelBio?)
}