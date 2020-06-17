package com.paypay.exchangerates.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.paypay.exchangerates.databinding.ItemExchangeRateBinding
import com.paypay.exchangerates.presentation.viewdata.ExchangeRateViewData

class ExchangeRateAdapter : ListAdapter<ExchangeRateViewData, ExchangeRateViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExchangeRateBinding.inflate(
            layoutInflater,
            parent,
            false
        )
        return ExchangeRateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExchangeRateViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ExchangeRateViewData>() {
            override fun areItemsTheSame(
                oldItem: ExchangeRateViewData,
                newItem: ExchangeRateViewData
            ): Boolean {
                return oldItem.currencyCode == newItem.currencyCode
            }

            override fun areContentsTheSame(
                oldItem: ExchangeRateViewData,
                newItem: ExchangeRateViewData
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}