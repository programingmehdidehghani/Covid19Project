package com.example.covid19project.util

import android.annotation.SuppressLint
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


@SuppressLint("SimpleDateFormat")
fun getPeriod(past: Date): String {
    val now = java.util.Date()
    val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
    val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

    return when {
        seconds < 60 -> {
            "Few seconds ago"
        }
        minutes < 60 -> {
            "$minutes minutes ago"
        }
        hours < 24 -> {
            "$hours hour ${minutes % 60} min ago"
        }
        else -> {
            SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString()
        }
    }

}


@SuppressLint("SimpleDateFormat")
fun String.toDateFormat(): java.util.Date? {
    return SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        .parse(this)
}