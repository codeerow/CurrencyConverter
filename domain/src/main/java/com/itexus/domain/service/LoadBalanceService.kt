package com.itexus.domain.service

import com.itexus.data.local.CurrenciesDao
import com.itexus.data.local.entity.CurrencyEntity
import com.itexus.domain.model.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoadBalanceService(
    private val dao: CurrenciesDao,
) {

    internal fun getBalances(): Flow<List<Currency>> = dao.readAll().map {
        it.map {
            Currency(
                it.name,
                it.value
            )
        }.sortedByDescending { it.value }.takeIf { it.isNotEmpty() } ?: listOf(Currency("EUR",
            START_BALANCE_VALUE))
            .also {
                dao.insertAll(
                    it.map { currency ->
                        CurrencyEntity(
                            name = currency.name,
                            value = currency.value
                        )
                    }
                )
            }.sortedByDescending { it.value }
    }

    companion object {
        private const val START_BALANCE_VALUE = 1000.0
    }
}
