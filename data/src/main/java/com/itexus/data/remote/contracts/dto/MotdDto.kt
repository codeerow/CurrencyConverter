package com.itexus.data.remote.contracts.dto

import com.google.gson.annotations.SerializedName

data class MotdDto(
    @SerializedName("msg")
    val message: String,

    @SerializedName("url")
    val url: String
)
