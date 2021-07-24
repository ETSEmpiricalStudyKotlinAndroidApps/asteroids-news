package com.klekchyan.asteroidsnews.utils

import java.text.SimpleDateFormat
import java.util.*

private const val NASA_API_RESPONSE_DATE_FORMAT = "yyyy-MMM-dd HH:mm"
private const val NASA_API_REQUEST_DATE_FORMAT = "yyyy-MM-dd"
private const val DATE_FORMAT = "yyyy.MM.dd"
private const val DATE_AND_TIME_FORMAT = "yyyy.MM.dd HH:mm"

enum class DateType(val format: String){ DATE(DATE_FORMAT), DATE_AND_TIME(DATE_AND_TIME_FORMAT)}

fun String.getDateFromNasaApiResponseFormat(): Date {
    val formatter = SimpleDateFormat(NASA_API_RESPONSE_DATE_FORMAT, Locale.ENGLISH)
    return formatter.parse(this) ?: Date()
}

fun Long.getDateStringForNasaApiRequest(): String {
    val formatter = SimpleDateFormat(NASA_API_REQUEST_DATE_FORMAT, Locale.ENGLISH)
    return formatter.format(this)
}

fun dateTypeCast(date: Date, dateType: DateType): String{
    val formatter = SimpleDateFormat(dateType.format, Locale.getDefault())
    return formatter.format(date)
}