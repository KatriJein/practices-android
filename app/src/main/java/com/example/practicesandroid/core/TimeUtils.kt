package com.example.practicesandroid.core

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

//fun tryParseServerDate(dateTime: String?): LocalDateTime? {
//    return try {
//        val zonedDateTime = java.time.ZonedDateTime.parse(dateTime)
//        zonedDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
//    } catch (e: DateTimeParseException) {
//        null
//    }
//}

fun tryParseServerDate(dateTime: String?): LocalDateTime? {
    if (dateTime.isNullOrEmpty()) return null

    return try {
        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val localDate = LocalDate.parse(dateTime, formatter)
            return localDate.atStartOfDay()
        } catch (e: DateTimeParseException) {
            val localDate = LocalDate.parse(dateTime)
            localDate.atStartOfDay()
        }
    } catch (e: DateTimeParseException) {
        null
    }
}

private fun formatDateOnly(dateTime: LocalDateTime): String {
    return DateTimeFormatter.ofPattern("dd.MM.yyyy").format(dateTime)
}

fun LocalDateTime?.orNow(): LocalDateTime {
    return this ?: LocalDateTime.now()
}