package com.itexus.domain.model

data class TransactionInfo(
    val from: String,
    val to: String,
    val amountBeforeConversion: Double,
    val amountAfterConversion: Double,
    val fee: Double,
)