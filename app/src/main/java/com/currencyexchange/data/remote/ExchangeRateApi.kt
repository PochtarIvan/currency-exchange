package com.currencyexchange.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {
    @GET("v1/tickers-currencies")
    suspend fun getTickerCurrencies(): List<String>

    @GET("v1/tickers")
    suspend fun getTickers(
        @Query("currencies") currencies: String
    ): List<CurrencyExchangeData>
}