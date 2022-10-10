package com.itexus.domain.usecase

import com.itexus.domain.model.Currency
import com.itexus.domain.service.LoadBalanceService
import kotlinx.coroutines.flow.Flow

class LoadBalanceUseCase(
    private val service: LoadBalanceService
) {

    operator fun invoke(): Flow<List<Currency>> = service.getBalances()
}
