package com.itexus.data.remote

import com.itexus.data.remote.contracts.GetRatesResponse
import retrofit2.http.GET

interface CurrenciesApi {

    @Throws(Exception::class)
    @GET("currency-exchange-rates")
    suspend fun getRates(): GetRatesResponse
}
