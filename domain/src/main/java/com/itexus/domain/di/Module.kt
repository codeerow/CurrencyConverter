package com.itexus.domain.di

import com.itexus.domain.repo.ExchangesCountRepository
import com.itexus.domain.repo.RatesRepository
import com.itexus.domain.service.LoadBalanceService
import com.itexus.domain.usecase.CalculateExchangeUseCase
import com.itexus.domain.usecase.ConvertCurrencyUseCase
import com.itexus.domain.usecase.GetCurrenciesUseCase
import com.itexus.domain.usecase.LoadBalanceUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val ratesRepo = module {
    single { RatesRepository(get()) }
    single { ExchangesCountRepository(androidContext()) }
}

private val useCases = module {
    single { GetCurrenciesUseCase(get()) }
    single { LoadBalanceUseCase(get()) }
    single { ConvertCurrencyUseCase(get(), get(), get(), get()) }
    single { CalculateExchangeUseCase(get()) }
}

private val services = module {
    single { LoadBalanceService(get()) }
}

val domainModule = listOf(ratesRepo, useCases, services)
