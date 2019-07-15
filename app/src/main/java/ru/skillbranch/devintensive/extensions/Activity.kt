package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard() {
    val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (isKeyboardOpen()) {
        imm.hideSoftInputFromWindow(currentView(this).windowToken, 0)
    }
}

fun Activity.isKeyboardOpen() = !isKeyboardClosed()

fun Activity.isKeyboardClosed() =  visibleHeight(this) <= 100

private fun currentView(activity: Activity) = activity.currentFocus ?: View(activity)

private fun visibleHeight(activity: Activity): Int {

    val rootView = currentView(activity).rootView

    val rect = Rect()
    rootView.getWindowVisibleDisplayFrame(rect)

    return rootView.height - (rect.bottom - rect.top)
}