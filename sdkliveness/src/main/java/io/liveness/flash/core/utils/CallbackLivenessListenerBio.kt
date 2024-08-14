package io.liveness.flash.core.utils

import androidx.annotation.Keep
import io.liveness.flash.core.model.LivenessModelBio

/**
 * Created by Thuytv on 16/04/2024.
 */
@Keep
interface CallbackLivenessListenerBio {
    fun onCallbackLiveness(data: LivenessModelBio?)
}