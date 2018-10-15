package it.emperor.todolist.extension

import android.app.Activity
import android.view.inputmethod.InputMethodManager

fun Activity.closeKeyboard() {
    val inputMethodManager: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}