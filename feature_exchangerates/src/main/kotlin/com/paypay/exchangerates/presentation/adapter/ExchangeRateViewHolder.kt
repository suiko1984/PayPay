package com.paypay.exchangerates.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.paypay.exchangerates.databinding.ItemExchangeRateBinding
import com.paypay.exchangerates.presentation.viewdata.ExchangeRateViewData

class ExchangeRateViewHolder(private val binding: ItemExchangeRateBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(repo: ExchangeRateViewData?) {
        binding.viewdata = repo
    }
}