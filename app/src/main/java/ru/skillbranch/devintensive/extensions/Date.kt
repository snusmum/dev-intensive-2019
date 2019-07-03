package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1
const val MINUTE = SECOND * 60
const val HOUR = MINUTE * 60
const val DAY = HOUR * 24

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String? {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time;

    time += 1000L * when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }

    this.time = time
    return this
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}

fun Date.humanizeDiff(): String {
    val diff = Date().time - this.time
    val absDiff = abs(diff) / 1000
    val seconds = absDiff / SECOND
    val minutes = absDiff / MINUTE
    val hours = absDiff / HOUR
    val days = absDiff / DAY
    val prefix = if (diff >= 0) "" else "через "
    val suffix = if (diff >= 0) " назад" else ""
    return when {
        days > 360 -> if (diff >= 0) "более года назад" else "более чем через год"
        hours > 26 -> "$prefix$days ${dayText(days)}$suffix"
        hours > 22 -> "${prefix}день$suffix"
        minutes > 75 -> "$prefix$hours ${hourText(hours)}$suffix"
        minutes > 45 -> "час$suffix"
        seconds > 75 -> "$prefix$minutes ${minuteText(minutes)}$suffix"
        seconds > 45 -> "${prefix}минуту$suffix"
        seconds > 1 -> "${prefix}несколько секунд$suffix"
        else -> "только что"
    }
//    return when {
//        seconds <= 1 && minutes == 0L && hours == 0L && days == 0L -> "только что"
//        seconds <= 45 && minutes == 0L && hours == 0L && days == 0L -> "${prefix}несколько секунд$suffix"
//        seconds <= 75 && minutes <= 1L && hours == 0L && days == 0L ->  "${prefix}минуту$suffix"
//        minutes <= 45 && hours == 0L && days == 0L -> "$prefix$minutes ${minuteText(minutes)}$suffix"
//        minutes <= 75 && hours <= 1L && days == 0L ->  "час$suffix"
//        hours <= 22 && days == 0L -> "$prefix$hours ${hourText(hours)}$suffix"
//        hours <= 26 && days <= 1L -> "${prefix}день$suffix"
//        days <= 360 -> "$prefix$days ${dayText(days)}$suffix"
//        else -> "более ${if (diff >=0) "" else "чем "}${prefix}года$suffix"
//    }
}

private fun minuteText(minute: Long): String {
    if (minute in 11..14) return "минут"
    return when (minute % 10) {
        1L -> "минуту"
        in 2..4 -> "минуты"
        else -> "минут"
    }
}

private fun hourText(hour: Long): String {
    if (hour in 11..14) return "часов"
    return when (hour % 10) {
        1L -> "час"
        in 2..4 -> "часа"
        else -> "часов"
    }
}

private fun dayText(day: Long): String {
    if (day in 11..14) return "дней"
    return when (day % 10) {
        1L -> "день"
        in 2..4 -> "дня"
        else -> "дней"
    }
}