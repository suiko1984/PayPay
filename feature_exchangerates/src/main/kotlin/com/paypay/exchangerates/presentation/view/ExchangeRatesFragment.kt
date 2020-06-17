package com.paypay.exchangerates.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.paypay.PayPayApp
import com.paypay.exchangerates.R
import com.paypay.exchangerates.databinding.FragmentExchangeRatesBinding
import com.paypay.exchangerates.di.DaggerExchangeRatesComponent
import com.paypay.exchangerates.presentation.adapter.CurrencyAdapter
import com.paypay.exchangerates.presentation.adapter.ExchangeRateAdapter
import com.paypay.exchangerates.presentation.extension.hideKeyboard
import com.paypay.exchangerates.presentation.viewdata.CurrencyViewData
import com.paypay.exchangerates.presentation.viewmodel.ExchangeRatesViewModel
import javax.inject.Inject

class ExchangeRatesFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val exchangeRatesViewModel: ExchangeRatesViewModel by viewModels { viewModelFactory }
    private val currencyAdapter: CurrencyAdapter by lazy {
        CurrencyAdapter(
            requireContext(),
            R.layout.item_currency
        )
    }
    private val exchangeRateAdapter: ExchangeRateAdapter by lazy { ExchangeRateAdapter() }

    override fun onAttach(context: Context) {
        val appComponent = (context.applicationContext as PayPayApp).appComponent
        DaggerExchangeRatesComponent.factory().create(appComponent, requireContext()).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        android.R.layout.simple_spinner_item
        return FragmentExchangeRatesBinding.inflate(inflater, container, false).apply {
            viewModel = exchangeRatesViewModel
            lifecycleOwner = this@ExchangeRatesFragment
            currencySpinner.adapter = currencyAdapter
            currencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(p0: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>, p1: View?, pos: Int, p3: Long) {
                    exchangeRatesViewModel.selectCurrency(parent.getItemAtPosition(pos) as CurrencyViewData)
                }
            }
            currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            amountEditText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) amountEditText.setText("")
            }
            amountEditText.setOnEditorActionListener { editText, action, event ->
                if (action == EditorInfo.IME_ACTION_DONE ||
                    event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    editText.hideKeyboard()
                    exchangeRatesViewModel.selectAmount(editText.text.toString().toDoubleOrNull())
                }
                false
            }
            convertButton.setOnClickListener {
                BaseInputConnection(it, true).performEditorAction(EditorInfo.IME_ACTION_DONE)
            }
            ratesRecyclerView.adapter = exchangeRateAdapter
        }.root
    }
}