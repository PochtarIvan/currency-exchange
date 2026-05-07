package com.currencyexchange.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class CurrencyExchangeModel(
    val currency: String,
    val ask: Double,
    val bid: Double,
    val date: String,
 ) {
    val exchangeRate: Double
        get() = ask
}
