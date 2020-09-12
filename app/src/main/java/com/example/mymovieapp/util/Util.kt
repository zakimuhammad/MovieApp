package com.example.mymovieapp.util

import java.util.*

fun getLocale(): String {
    val locale = Locale.getDefault().language
    var language = "language"
    if (locale == "en") {
        language = "en-US"
    } else if (locale == "in") {
        language = "id-IN"
    }
    return language
}