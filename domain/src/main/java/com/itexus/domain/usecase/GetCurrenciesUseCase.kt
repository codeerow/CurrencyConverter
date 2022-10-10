package com.itexus.domain.usecase

import com.itexus.domain.model.Rate
import com.itexus.domain.repo.RatesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCurrenciesUseCase(
    private val repo: RatesRepository
) {

    operator fun invoke(): Flow<List<Rate>> = flow {
        emit(repo.getRates())
    }
}
