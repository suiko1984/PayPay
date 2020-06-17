package com.paypay.exchangerates.presentation.bindingadapter

import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("amount")
fun amount(view: EditText, amount: Double) {
    view.setText(amount.toString())
    view.hint = amount.toString()
}