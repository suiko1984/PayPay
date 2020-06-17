package com.paypay.exchangerates.presentation.bindingadapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paypay.exchangerates.presentation.viewdata.ExchangeRateViewData
import com.paypay.exchangerates.presentation.adapter.ExchangeRateAdapter

@BindingAdapter("exchangerates")
fun exchangerates(view: RecyclerView, exchangerates: List<ExchangeRateViewData>) {
    (view.adapter as ExchangeRateAdapter).submitList(exchangerates)
}