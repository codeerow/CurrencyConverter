package com.itexus.domain.usecase

import com.itexus.domain.repo.RatesRepository

class CalculateExchangeUseCase(
    private val ratesRepo: RatesRepository,
) {

    operator fun invoke(
        from: String,
        to: String,
        amount: Double,
    ): Double {
        if (from == to) return amount

        val rates = ratesRepo.getLastRates()
        val rateFrom = rates.find { it.name == from }
        val rateTo = rates.find { it.name == to } ?: return amount

        return if (rateFrom?.base != null) {
            val currencyInBase = amount / rateFrom.value
            currencyInBase * rateTo.value
        } else {
            amount * rateTo.value
        }
    }
}
