package com.paypay.exchangerates.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.paypay.exchangerates.presentation.viewdata.CurrencyViewData

class CurrencyAdapter(
    context: Context,
    resource: Int
) : ArrayAdapter<CurrencyViewData>(context, resource) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = super.getView(position, convertView, parent) as TextView
        textView.text = getItem(position)?.description
        return textView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView = super.getDropDownView(position, convertView, parent) as TextView
        textView.text = getItem(position)?.description
        return textView
    }
}