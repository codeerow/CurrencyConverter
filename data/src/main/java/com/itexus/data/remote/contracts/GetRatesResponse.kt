package com.itexus.data.remote.contracts

import com.google.gson.annotations.SerializedName
import com.itexus.data.remote.contracts.dto.MotdDto
import java.util.*

data class GetRatesResponse(
    @SerializedName("motd")
    val motd: MotdDto,

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("base")
    val base: String,

    @SerializedName("date")
    val date: Date,

    @SerializedName("rates")
    val rates: List<Map<String, Double>>
)
