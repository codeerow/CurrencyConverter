package com.itexus.data.remote

import com.itexus.data.remote.contracts.GetRatesResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CurrenciesApi {

    @Throws(Exception::class)
    @GET("latest")
    fun getRates(): Single<GetRatesResponse>
}
