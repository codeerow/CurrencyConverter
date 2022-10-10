package com.itexus.data.remote.contracts

import com.google.gson.annotations.SerializedName

data class GetRatesResponse(

    @SerializedName("base")
    val base: String,

    @SerializedName("date")
    val date: String,

    @SerializedName("rates")
    val rates: Map<String, Double>
)
