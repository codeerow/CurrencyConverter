package com.itexus.ui.screen.currencyconverter

import androidx.lifecycle.ViewModel
import com.itexus.ui.component.list.currencies.CurrencyItemModel
import kotlinx.coroutines.flow.Flow

abstract class CurrencyConverterViewModelApi : ViewModel() {

    abstract val balances: Flow<List<CurrencyItemModel>>
    abstract val ratesList: Flow<List<String>>
    abstract val transactionMessage: Flow<String>
    abstract val receivedAmount: Flow<String>
    abstract val isSubmitAvailable: Flow<Boolean>
    abstract val error: Flow<String>

    abstract fun onSellAmountChanged(value: String)
    abstract fun onFromChanged(item: String?)
    abstract fun onToChanged(item: String?)
    abstract fun submit()
}
