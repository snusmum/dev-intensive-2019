package ru.skillbranch.devintensive.extensions

fun String?.blankToNull(): String? {
    return if (this.isNullOrBlank()) null else this
}

fun String.truncate(limit: Int = 16): String {
    return when {
        this.length <= limit -> this
        else -> this.substring(0, limit).trimEnd() + "..."
    }
}