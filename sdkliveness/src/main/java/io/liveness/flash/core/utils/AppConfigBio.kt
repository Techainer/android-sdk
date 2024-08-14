package io.liveness.flash.core.utils

import android.annotation.SuppressLint
import android.view.View
import io.liveness.flash.core.model.LivenessRequestBio

/**
 * Created by Thuytv on 16/04/2024.
 */
@SuppressLint("StaticFieldLeak")
internal object AppConfigBio {
    var livenessListener: CallbackLivenessListenerBio? = null
    var livenessFaceListener: CallbackLivenessListenerBio? = null
    var mLivenessRequest: LivenessRequestBio? = null
    var mOptionRequest: LivenessRequestBio? = null

    var mCustomView: View? = null
    var mActionView: View? = null
    var mProgressView: View? = null

    var KEY_BUNDLE_SCREEN = "KEY_BUNDLE_SCREEN"
    var TYPE_SCREEN_REGISTER_FACE = "TYPE_SCREEN_REGISTER_FACE"
    var INTENT_VALUE_BACK = "INTENT_VALUE_BACK"

    val encrypted_register_face = "gH7p6MLKXOZP/Ln6cYJYcq4cu0n/BF7I2WEf+B4h158NMOB+Ht9knp+qs0lFZebKdCPos0qfhMGH\n" +
            "oA0MSCkoHpW7/VF7GtJOhnVqSjHZcNrluk/Z8MYFcsrEPCik0dTy+VUBegbeD15cUzkY3B6aTUy6\n" +
            "v+8XalWj3rmOVisExMo="
    val encrypted_init_transaction = "ZxSCj3ReZ2Cee6zQwKJ1J7zfW1iOJ6COGMxZ7nA1JRx6Ib1yRjJPGs26wWNpMrFNqWZThN/Jhwex\n" +
            "nG6Jts9oPqmkvEObqkPinknXGIM5LslqlD/I/rvKNpkXpU2BOfy+5UkIocWq9nAFBFdMzRYuE0MM\n" +
            "h4zihVkU4zTRXxIBppc="
    val encrypted_register_device = "V+Lu9Lc9+nrBQqDnWbtFbKr5je/RGdBmrsdU+OH78iNjJHqvDWhYeM+KfduK0My0zyZfNWfS1o/I\n" +
            "nKOvJXznWOgHwd/MGfo2a8qR63taBD6oHURPFQ0maNrye5JObozu3etmu3NWTXd3d3U3OO3zgjgk\n" +
            "S/GZAhpWGhmx+J6/PSI="
    val encrypted_verify_face = "Zg7B5wh5ZwN5smyS6bMAi65s8+GviMTxKea2XKlvGvFEEz8e6DN4wco4HCBnKYdZNrhSlkx3E5s+\n" +
            "naqglipSC/ZmYkd9Ayb0y0I1gPM7uupKuZ4gnUSSflo89ty8twYTkgPR15h7fnnwNBgG+INQJMEz\n" +
            "8iintilD+m1lnQmvTL0="
    val encrypted_check_live_ness = "M22TJoIQtxN9s4zxFby1QAWBdtT6auEbAfpzEt0soE/tSG6c9Dp9xr60rpQB99hGYOQ0W+pMqQNw\n" +
            "vkv39fe24CoY2SnaBzT573cXwbVgSQqDmyMQCeSOwwd6THEwVg3mKvdoaaeMvivU4+f8mChfzHOv\n" +
            "dIpX4WP7xOY71lnjsU4="
    val encrypted_deviceId = "YVKNrtJLcTNVm8DbMdo25IdRV4CCFRFD3vBt9gH6h4UudUjqjpVIsZNHtYXRIwFWOvwqYonbqvaz\n" +
            "hLrOU5nOGtK5ZUoBv1L5Pg8y/zw2r5TcD2RSQn5zV5BVmQ1KTEn2S2VyGI0UWrsg8ESLPMW2CmaG\n" +
            "PaOeALOmDUCkBf4wH2w="
    val encrypted_deviceOS = "HevislQYw7OVQaruzLRhZP1vWW99xT1Ln3ySLuKEWz8UWSgIVmnzOvbNxMZyFKz76uDygzQ/d7i5\n" +
            "G7iLyi5K8PpaZT/eqtdC+xWEHQAJ27Jf5735BLI0pZ/Kxgv6vSFklA86sM0KAY2kUiuDgm4+qXKO\n" +
            "UMKlqevFXIE3RLiA3F4="
    val encrypted_device_name = "ZfscIGMgKwT6Kaset81F7kWFAjbsOi19XlfkaUcnPQSGxLoq6n1YII8p18VlbttyPOQAof20uc0w\n" +
            "A6AQXRKfT03Q9gj7ttGlvmB6+ZIdf08Bf7i1RoGizR6J8/UQ6TW2rvil3M97EdU1cU7nNe+87MHX\n" +
            "iFmWNx7XtEuDk1pBStk="
    val encrypted_period = "KOHrEhXL4ag/1bU311BkwOJAHyYgawPhHRxUfA7C1D+3N+aLn0xL5TG5uzAwMcCv92VCuAKbRTcX\n" +
            "WpxWqhvL4w/2Fu68P4I80pZUPe6tqD9g1FAfLhDCQBJw4LYLvN4nWjzSfja+5zkak0CGY+QZYTHc\n" +
            "atCIZDShFV4cSABpBfQ="
    val encrypted_secret = "eXgbHtBwRuYYwf2QT4/VMsIz0K/mqEmPXzDFYic1TdWu1BZauw8Ggy75QP+DWihkH1WOHzSOzPe3\n" +
            "5i6ir4tPxpCs6ttb/ZBu5a2hnDNCsmvIKWaaYNZABQZFw/L+eqaQAXseRZFAJQvsnwH2yKTpbRlF\n" +
            "Y0taqtraQ+N8rAt5rvM="
    val encrypted_face_image = "devw6gbrpvl5CJoEOSrTpQrxW49jiraBwB41JjSoyVt5jXJbpduwIMeDdLoxtz0QmT9k214Ccv7y\n" +
            "PNOed/JFt9Xq4hKzY667SVLhlQWQKb7b+9T2Hz2pCbjj2uSvzB6hA/fpxW4tptT88Qj8M5rGloV+\n" +
            "6RvIcUjjPsosDmj+RLk="
    val encrypted_device_id = "bfEaexMNS6QnwVzHSkw9pusuf/OJ1Za8jEoAbnfYVuGBY3ANKXJUqD7iNC5BI9oOP9cBjGZe9ghh\n" +
            "m1yeYbf4+o7kHtdSSOLDYEW/g9cip/y2XyYwXp+3y4i+dhOyH4cy8DQEAySjr4rXb5hOyQkAVjS9\n" +
            "6P3JJosP4DOJh5ux4Vg="
    val encrypted_totp = "joIv0FuSimsdRA6+Zsl5BDDW3nZqHmcB4dTm/lDENGJfHKDyVN3fGIsPxvzGrn+i2Vbo0ZqcdFOW\n" +
            "OrhjeP5MInXSKcTZAz31DXC+hJZnlLoeHdAZVR9iUZ2Ac5flu74mPV0Y8E3jdV7aawftq9O1jadE\n" +
            "IWGmyfdQ2wbtd/pC8aQ="
    val encrypted_transaction_id = "eZDLR5WFhY0SgpW+CxpGYLx9IuXvNoXlTR6xUBYeg+HvYMoeGofq4E2lDZvXwvBbvNI4aVMw5H89\n" +
            "qjUihZtGRlDZU3G30NIJZD+o+quaEYZMMzFkAeGw3Wpiq9LnfzXi7DnmDRgFtJrXwJQNwBBizSIm\n" +
            "T1lyYtDN+wIHSLE5CQM="
    val encrypted_image_live = "TgWHr0tz5yeJl7R/LAwkNhbd0bXQqwIWJvgk7DGWc5WrotFpSKOvpwsnAWAw/ei/GepadhZ0Yf6C\n" +
            "NNCTu/AQBBqBeqs1vRcUWJ2MWcC3AKhfXYUBgIczGCW5R1TVMajDmgH5QOHejA+P3P/qn7zdbsJr\n" +
            "/V7I4/LK3Pbp+6xctP0="
    val encrypted_color = "Hil5dpT0hnrFWZklyVCvxi7JqX4P0daZcckCZjaROKZPTKoI1r11pu8WXgTO/I/4eDGKpR4CPkmy\n" +
            "KxpzxM/87fGOY0afrHRfIQeBspuNpzzFnyixxDwLQ/EL2QWn/6QPijIgOfyd+NqBzR2r9XBfhxRB\n" +
            "GkZfx9clZ8yvfM95Ouc="
    val encrypted_clientTransactionId = "CypAqo8/nBKT+Ho97Yzy0lPJYvgsl8bGPNr0wQw4TsSIaaLXOmFa+tn6e11LodQnwVSmlgQq6nR5\n" +
            "h6/YtzYVYE1pHlZxOsOy7GklYNbVnTr4oU1/sRsGE6ldR0X45PnBDknn00AhqTOBL/kRfXK+NsQG\n" +
            "THO3KownmJfPeizLaVY="

    val key_encrypted_register_face = "key_encrypted_a"
    val key_encrypted_init_transaction = "key_encrypted_b"
    val key_encrypted_register_device = "key_encrypted_c"
    val key_encrypted_verify_face = "key_encrypted_d"
    val key_encrypted_deviceId = "key_encrypted_e"
    val key_encrypted_deviceOS = "key_encrypted_f"
    val key_encrypted_device_name = "key_encrypted_g"
    val key_encrypted_period = "key_encrypted_h"
    val key_encrypted_secret = "key_encrypted_i"
    val key_encrypted_face_image = "key_encrypted_k"
    val key_encrypted_device_id = "key_encrypted_l"
    val key_encrypted_totp = "key_encrypted_m"
    val key_encrypted_transaction_id = "key_encrypted_n"
    val key_encrypted_image_live = "key_encrypted_s"
    val key_encrypted_color = "key_encrypted_x"
    val key_encrypted_clientTransactionId ="key_encrypted_z"
}