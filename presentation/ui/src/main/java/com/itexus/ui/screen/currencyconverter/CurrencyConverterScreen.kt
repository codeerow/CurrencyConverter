package com.itexus.ui.screen.currencyconverter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.itexus.ui.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyConverterScreen : Fragment(R.layout.screen_currency_converter) {

    private val viewModel: CurrencyConverterViewModelApi by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}
