package com.paypay.common.presentation.extension

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import android.widget.TextView

fun TextView.hideKeyboard() {
    clearFocus()
    (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
        windowToken,
        0
    )
}