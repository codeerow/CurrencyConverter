package com.itexus.logic

import com.itexus.logic.viewmodel.CurrencyConverterViewModel
import com.itexus.ui.screen.currencyconverter.CurrencyConverterViewModelApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel<CurrencyConverterViewModelApi> {
        CurrencyConverterViewModel()
    }
}
