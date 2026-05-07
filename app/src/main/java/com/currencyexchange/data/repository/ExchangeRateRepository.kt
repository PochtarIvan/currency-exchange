package com.currencyexchange.data.repository

import com.currencyexchange.model.CurrencyCode
import com.currencyexchange.model.CurrencyExchangeModel

internal interface ExchangeRateRepository {
    suspend fun getAvailableCurrencies(): List<CurrencyCode>
    suspend fun getExchangeRates(
        currencies: List<CurrencyCode>,
    ): Result<List<CurrencyExchangeModel>>
}