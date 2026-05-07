package com.currencyexchange.data.remote

import kotlinx.serialization.Serializable


@Serializable
data class CurrencyExchangeData(
    val ask: String,
    val bid: String,
    val book: String,
    val date: String,
)