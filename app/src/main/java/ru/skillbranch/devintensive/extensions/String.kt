package ru.skillbranch.devintensive.extensions

fun String?.blankToNull(): String? {
    return if (this.isNullOrBlank()) null else this
}