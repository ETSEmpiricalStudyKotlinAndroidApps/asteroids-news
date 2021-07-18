package com.klekchyan.asteroidsnews.utils

import java.text.SimpleDateFormat
import java.util.*

private const val NASA_API_DATE_FORMAT = "yyyy-MMM-dd HH:mm"
private const val DATE_FORMAT = "yyyy.MM.dd"
private const val DATE_AND_TIME_FORMAT = "yyyy.MM.dd HH:mm"

enum class DateType(val format: String){ DATE(DATE_FORMAT), DATE_AND_TIME(DATE_AND_TIME_FORMAT)}

fun getDateFromString(dateString: String): Date {
    val formatter = SimpleDateFormat(NASA_API_DATE_FORMAT, Locale.ENGLISH)
    return formatter.parse(dateString) ?: Date()
}

fun dateTypeCast(date: Date, dateType: DateType): String{
    val formatter = SimpleDateFormat(dateType.format, Locale.getDefault())
    return formatter.format(date)
}