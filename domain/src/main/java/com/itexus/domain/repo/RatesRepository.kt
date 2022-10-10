package com.itexus.domain.repo

import com.itexus.data.remote.CurrenciesApi
import com.itexus.domain.model.Rate

class RatesRepository(private val api: CurrenciesApi) {
    private var rates = emptyList<Rate>()

    suspend fun getRates(): List<Rate> = with(api.getRates()) {
        return rates.keys.map {
            Rate(it, base, rates.getValue(it))
        }.also { this@RatesRepository.rates = it }
    }

    fun getLastRates(): List<Rate> = rates
}
