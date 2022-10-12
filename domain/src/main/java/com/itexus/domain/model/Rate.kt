package com.itexus.domain.model

import java.math.BigDecimal

data class Rate(
    val name: String,
    val base: String,
    val value: BigDecimal
)
