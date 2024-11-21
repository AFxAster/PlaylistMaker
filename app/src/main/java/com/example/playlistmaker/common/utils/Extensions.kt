package com.example.playlistmaker.common.utils

import java.text.SimpleDateFormat
import java.util.Locale


fun Long.toFormattedTime(): String {
    return SimpleDateFormat(
        "m:ss",
        Locale.getDefault()
    ).format(this)
}

fun Int.toFormattedTime(): String {
    return SimpleDateFormat(
        "m:ss",
        Locale.getDefault()
    ).format(this)
}