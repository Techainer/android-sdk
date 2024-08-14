package com.liveness.sdk.core.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

internal object DateTimeUtilBio {
    fun formatDate(inputDate: String?, inputFormatStr: String?, outputFormatStr: String?): String {
        val inputFormat = SimpleDateFormat(inputFormatStr)
        val outputFormat = SimpleDateFormat(outputFormatStr)
        var outputDate = ""
        outputDate = try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            ""
        }
        return outputDate
    }

    fun toISO8601UTC(date: Date?): String {
        val tz = TimeZone.getTimeZone("UTC")
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        df.timeZone = tz
        return df.format(date)
    }

    fun fromISO8601UTC(dateStr: String?): Date? {
        val tz = TimeZone.getTimeZone("UTC")
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        df.timeZone = tz
        try {
            return df.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }
}