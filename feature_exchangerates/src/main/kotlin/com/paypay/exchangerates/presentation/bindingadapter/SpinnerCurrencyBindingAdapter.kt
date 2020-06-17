package com.paypay.exchangerates.presentation.bindingadapter

import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.paypay.exchangerates.presentation.adapter.CurrencyAdapter
import com.paypay.exchangerates.presentation.viewdata.CurrencyViewData

@Suppress("UNCHECKED_CAST")
@BindingAdapter("currencies", "selectedCurrency")
fun currencies(view: Spinner, currencies: List<CurrencyViewData>, selectedCurrency: CurrencyViewData?) {
    val adapter = view.adapter as CurrencyAdapter
    adapter.clear()
    adapter.addAll(currencies)
    selectedCurrency?.let { view.setSelection(currencies.indexOf(it)) }
}