package com.paypay.exchangerates.presentation.bindingadapter

import android.widget.ViewFlipper
import androidx.databinding.BindingAdapter

@BindingAdapter("hasError", "isLoading")
fun isLoading(view: ViewFlipper, hasError: Boolean, isLoading: Boolean) {
    view.displayedChild = if (isLoading) { 0 } else if (hasError) { 1 } else { 2 }
}

@BindingAdapter("ratesHasError", "ratesIsLoading")
fun ratesIsLoading(view: ViewFlipper, ratesHasError: Boolean, ratesIsLoading: Boolean) {
    view.displayedChild = if (ratesIsLoading) { 0 } else if (ratesHasError) { 2 } else { 1 }
}