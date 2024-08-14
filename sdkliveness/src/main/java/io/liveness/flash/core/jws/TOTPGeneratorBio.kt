package io.liveness.flash.core.jws

import android.util.Log
import io.liveness.flash.core.utils.AppConfigBio
import io.liveness.flash.core.utils.codec.Base32Bio
import java.nio.ByteBuffer
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class TOTPGeneratorBio {
    fun generateTOTP(secret: String): String {
        return try {
            val base32 = Base32Bio()
            // Ensure the secret is base32 encoded
//            val keyBytes = base32.decode(testSecret)
            val keyBytes = base32.decode(secret)
            // Calculate the number of time steps (30 seconds intervals) since Unix epoch
            if(keyBytes.isEmpty()){
                return "-1"
            }
            val period = AppConfigBio.mLivenessRequest?.duration ?: 30
            Log.d("Thuytv","-----period: $period")
//            val timeStep = System.currentTimeMillis() / (TIME_STEP_SECONDS * 1000)
            val timeStep = System.currentTimeMillis() / (period * 1000)

            // Prepare the byte buffer with time step value
            val buffer = ByteBuffer.allocate(8)
            buffer.putLong(timeStep)
            val timeStepBytes = buffer.array()

            // Generate the HMAC-SHA1 hash using the secret and time step bytes
            val keySpec = SecretKeySpec(keyBytes, TOTP_ALGORITHM)
            val mac = Mac.getInstance(TOTP_ALGORITHM)
            mac.init(keySpec)
            val hashBytes = mac.doFinal(timeStepBytes)

            // Calculate the offset in the hash value based on the last 4 bits
            val offset = hashBytes[hashBytes.size - 1].toInt() and 0xF

            // Extract a 4-byte subsequence from the hash
            var truncatedHash = ByteBuffer.wrap(hashBytes, offset, 4).int
            truncatedHash = truncatedHash and 0x7FFFFFFF // Remove the highest bit for 31-bit integer

            // Calculate the final TOTP value
            val totpDigits = DEFAULT_TOTP_DIGITS
            val modValue = Math.pow(10.0, totpDigits.toDouble()).toInt()
            var otp = (truncatedHash % modValue).toString()
            while (otp.length < totpDigits) {
                otp = "0$otp" // Prepend leading zeros if necessary
            }
            otp
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
            ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    companion object {
        private const val DEFAULT_TOTP_DIGITS = 6
        private const val TIME_STEP_SECONDS: Long = 30
        private const val TOTP_ALGORITHM = "HmacSHA1"
    }
}