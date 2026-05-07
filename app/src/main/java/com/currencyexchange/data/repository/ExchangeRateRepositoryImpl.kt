package com.currencyexchange.data.repository

import com.currencyexchange.data.remote.CurrencyExchangeData
import com.currencyexchange.data.remote.ExchangeRateApi
import com.currencyexchange.di.IoDispatcher
import com.currencyexchange.mapper.toModel
import com.currencyexchange.model.CurrencyCode
import com.currencyexchange.model.CurrencyExchangeModel
import com.currencyexchange.model.toCurrencyCodeOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ExchangeRateRepository {

    override suspend fun getAvailableCurrencies(): List<CurrencyCode> = withContext(ioDispatcher) {
        runCatching {
            api.getTickerCurrencies()
        }.getOrElse {
            fallbackCurrencies
        }.mapNotNull { it.toCurrencyCodeOrNull() }
    }

    override suspend fun getExchangeRates(
        currencies: List<CurrencyCode>,
    ): Result<List<CurrencyExchangeModel>> = withContext(ioDispatcher) {
        runCatching {
            api.getTickers(
                currencies.joinToString(",") { it.apiCode }
            ).map(CurrencyExchangeData::toModel)
        }
    }

    private companion object {
        val fallbackCurrencies = listOf("MXN", "ARS", "BRL", "COP")
    }
}