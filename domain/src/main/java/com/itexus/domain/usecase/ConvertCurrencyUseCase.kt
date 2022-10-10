package com.itexus.domain.usecase

import com.itexus.data.local.CurrenciesDao
import com.itexus.data.local.entity.CurrencyEntity
import com.itexus.domain.model.CurrencyConverterException
import com.itexus.domain.model.TransactionInfo
import com.itexus.domain.repo.ExchangesCountRepository
import com.itexus.domain.repo.RatesRepository
import com.itexus.domain.service.LoadBalanceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class ConvertCurrencyUseCase(
    private val exchangesCountRepo: ExchangesCountRepository,
    private val ratesRepo: RatesRepository,
    private val loadBalanceService: LoadBalanceService,
    private val dao: CurrenciesDao,
) {

    suspend operator fun invoke(
        from: String,
        to: String,
        amount: Double,
    ): TransactionInfo? {
        return withContext(Dispatchers.IO) {
            if (from == to) return@withContext null

            val commissionFee = exchangesCountRepo.getCommissionFee()
            val fromRate = ratesRepo.getLastRates().find { it.name == from }
                ?: throw CurrencyConverterException("Rate not found")
            val toRate = ratesRepo.getLastRates().find { it.name == to }
                ?: throw CurrencyConverterException("Rate not found")

            val baseAmount = amount / fromRate.value
            val amountAfterConversion = baseAmount * toRate.value
            val fee = amountAfterConversion * commissionFee

            val fromInitialBalance = loadBalanceService.getBalances().first()
                .find { it.name == from }
                ?: throw CurrencyConverterException("Current balance is empty")
            val toInitialBalance = loadBalanceService.getBalances().first()
                .find { it.name == to }

            if (baseAmount + fee > fromInitialBalance.value / fromRate.value) {
                throw CurrencyConverterException("You don't have that much currency")
            }

            dao.insert(
                CurrencyEntity(
                    name = fromInitialBalance.name,
                    value = fromInitialBalance.value - amount - fee / fromRate.value),
            )
            dao.insert(
                CurrencyEntity(
                    name = to,
                    value = amountAfterConversion + (toInitialBalance?.value ?: 0.0)),
            )
            exchangesCountRepo.addExchangesCount()
            return@withContext TransactionInfo(
                from = from,
                to = to,
                amountBeforeConversion = amount,
                amountAfterConversion = amountAfterConversion + (toInitialBalance?.value ?: 0.0),
                fee = fee,
            )
        }
    }
}
