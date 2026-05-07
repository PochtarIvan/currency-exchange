package com.currencyexchange.data.repository

import com.currencyexchange.model.CurrencyExchangeModel

internal interface ExchangeRateRepository {
    suspend fun getAvailableCurrencies(): List<String>
    suspend fun getExchangeRates(
        currencies: List<String>
    ): Result<List<CurrencyExchangeModel>>
}