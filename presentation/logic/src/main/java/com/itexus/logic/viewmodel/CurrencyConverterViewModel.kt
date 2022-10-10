package com.itexus.logic.viewmodel

import androidx.lifecycle.viewModelScope
import com.itexus.domain.model.CurrencyConverterException
import com.itexus.domain.model.TransactionInfo
import com.itexus.domain.usecase.CalculateExchangeUseCase
import com.itexus.domain.usecase.ConvertCurrencyUseCase
import com.itexus.domain.usecase.GetCurrenciesUseCase
import com.itexus.domain.usecase.LoadBalanceUseCase
import com.itexus.logic.core.tickerFlow
import com.itexus.ui.component.list.currencies.CurrencyItemModel
import com.itexus.ui.screen.currencyconverter.CurrencyConverterViewModelApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
class CurrencyConverterViewModel(
    private val getCurrenciesUseCase: GetCurrenciesUseCase,
    private val loadBalanceUseCase: LoadBalanceUseCase,
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val calculateExchangeUseCase: CalculateExchangeUseCase,
) : CurrencyConverterViewModelApi() {

    companion object {
        private const val RATE_SYNCHRONIZATION_PERIOD = 5000L
    }

    private val amount: MutableStateFlow<Double?> = MutableStateFlow(null)
    private val from: MutableStateFlow<String?> = MutableStateFlow(null)
    private val to: MutableStateFlow<String?> = MutableStateFlow(null)

    override val balances: MutableStateFlow<List<CurrencyItemModel>> = MutableStateFlow(listOf())
    override val ratesList: MutableStateFlow<List<String>> = MutableStateFlow(listOf())
    override val transactionMessage: MutableSharedFlow<String> = MutableSharedFlow()
    override val error: MutableSharedFlow<String> = MutableSharedFlow()
    override val isSubmitAvailable = combine(amount, from, to)
    { amount, to, from -> amount != null && to != null && from != null }

    override val receivedAmount: Flow<String> = combine(from, to, amount) { from, to, amount ->
        return@combine if (from != null && to != null && amount != null) {
            "+${calculateExchangeUseCase(from, to, amount)}"
        } else {
            ""
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        if (throwable is CurrencyConverterException) {
            error.tryEmit(throwable.message)
        } else {
            error.tryEmit("Something went wrong")
        }
    }


    init {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            combine(
                tickerFlow(millis = RATE_SYNCHRONIZATION_PERIOD)
                    .flatMapConcat { getCurrenciesUseCase() },
                loadBalanceUseCase(),
            ) { currencies, balance -> currencies to balance }.collect { (currencies, balance) ->
                ratesList.emit(currencies.map { rate -> rate.name })
                balances.emit(balance.map { currency ->
                    CurrencyItemModel(currency.name, currency.value)
                })
            }
        }
    }

    override fun onFromChanged(item: String?) = run { from.value = item }
    override fun onToChanged(item: String?) = run { to.value = item }

    override fun onSellAmountChanged(value: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            try {
                amount.emit(value.toDouble())
            } catch (e: Exception) {
                amount.emit(null)
            }
        }
    }

    override fun submit() {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val transactionInfo = combine(from, to, amount) { from, to, amount ->
                convertCurrencyUseCase(
                    requireNotNull(from),
                    requireNotNull(to),
                    requireNotNull(amount),
                )
            }.first()

            transactionInfo?.let {
                val message = buildTransactionMessage(info = it)
                transactionMessage.emit(message)
            }
        }
    }

    private fun buildTransactionMessage(info: TransactionInfo): String {
        val exchangeMessage = "You have converted ${info.amountBeforeConversion} ${info.from} to" +
            " ${info.amountAfterConversion} ${info.to}."
        val commissionMessage = " Commission Fee - ${info.fee} ${info.from}."
        val message = if (info.fee == 0.0) {
            exchangeMessage
        } else {
            exchangeMessage + commissionMessage
        }
        return message
    }
}