package io.liveness.flash.core.utils

import androidx.annotation.Keep

/**
 * Created by Thuytv on 16/04/2024.
 */
@Keep
interface CallbackAPIListenerBio {
    fun onCallbackResponse(data: String?)
}