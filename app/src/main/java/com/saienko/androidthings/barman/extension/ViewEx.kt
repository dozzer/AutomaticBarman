package com.saienko.androidthings.barman.extension

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 1/16/18
 * Time: 00:06
 */

//fun EditText.hideKeyboard() {
//    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    imm.hideSoftInputFromWindow(this.windowToken, 0)
//}


fun Activity.hideKeyboard() {
    val view = this.currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}