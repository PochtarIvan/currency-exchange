package com.currencyexchange.data.repository

import com.currencyexchange.data.remote.CurrencyExchangeData
import com.currencyexchange.data.remote.ExchangeRateApi
import com.currencyexchange.di.IoDispatcher
import com.currencyexchange.mapper.toModel
import com.currencyexchange.model.CurrencyExchangeModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ExchangeRateRepository {

    override suspend fun getAvailableCurrencies(): List<String> = withContext(ioDispatcher) {
        runCatching {
            api.getTickerCurrencies()
        }.getOrElse {
            fallbackCurrencies
        }
    }

    override suspend fun getExchangeRates(
        currencies: List<String>
    ): Result<List<CurrencyExchangeModel>> = withContext(ioDispatcher) {
        runCatching {
            api.getTickers(currencies.joinToString(","))
                .map(CurrencyExchangeData::toModel)
        }
    }

    private companion object {
        val fallbackCurrencies = listOf("MXN", "ARS", "BRL", "COP")
    }
}