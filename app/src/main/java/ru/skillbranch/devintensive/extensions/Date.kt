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
    var time = this.time

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
    SECOND {
        override fun plural(value: Int): String {
            return pluralInner(value, "секуну", "секунды", "секунд")
        }
    },
    MINUTE {
        override fun plural(value: Int): String {
            return pluralInner(value, "минуту", "минуты", "минут")
        }
    },
    HOUR {
        override fun plural(value: Int): String {
            return pluralInner(value, "час", "часа", "часов")
        }
    },
    DAY {
        override fun plural(value: Int): String {
            return pluralInner(value, "день", "дня", "дней")
        }
    };

    abstract fun plural(value: Int): String

    internal fun pluralInner(value: Int, oneText: String, lessFiveText: String, othersText: String): String {

        fun valWithText(t: String): String = "$value $t"

        val stringValue = value.toString()
        if (stringValue.endsWith("11")
            || stringValue.endsWith("12")
            || stringValue.endsWith("13")
            || stringValue.endsWith("14")
        ) return valWithText(othersText)

        return valWithText(
            when (value % 10) {
                1 -> oneText
                in 2..4 -> lessFiveText
                else -> othersText
            }
        )
    }
}

fun Date.humanizeDiff(): String {
    val diff = Date().time - this.time
    val absDiff = abs(diff) / 1000
    val seconds: Int = (absDiff / SECOND).toInt()
    val minutes: Int = (absDiff / MINUTE).toInt()
    val hours: Int = (absDiff / HOUR).toInt()
    val days: Int = (absDiff / DAY).toInt()
    val prefix = if (diff >= 0) "" else "через "
    val suffix = if (diff >= 0) " назад" else ""
    return when {
        days > 360 -> if (diff >= 0) "более года назад" else "более чем через год"
        hours > 26 -> "$prefix${TimeUnits.DAY.plural(days)}$suffix"
        hours > 22 -> "${prefix}день$suffix"
        minutes > 75 -> "$prefix${TimeUnits.HOUR.plural(hours)}$suffix"
        minutes > 45 -> "час$suffix"
        seconds > 75 -> "$prefix${TimeUnits.MINUTE.plural(minutes)}$suffix"
        seconds > 45 -> "${prefix}минуту$suffix"
        seconds > 1 -> "${prefix}несколько секунд$suffix"
        else -> "только что"
    }
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