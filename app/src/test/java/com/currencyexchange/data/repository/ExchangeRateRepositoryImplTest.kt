package com.currencyexchange.data.repository

import com.currencyexchange.data.remote.CurrencyExchangeData
import com.currencyexchange.data.remote.ExchangeRateApi
import com.currencyexchange.model.CurrencyCode
import com.currencyexchange.model.CurrencyExchangeModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ExchangeRateRepositoryImplTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var api: ExchangeRateApi
    private lateinit var repository: ExchangeRateRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        repository = ExchangeRateRepositoryImpl(api, testDispatcher)
    }

    @Test
    fun `GIVEN api returns valid currencies WHEN get available currencies THEN returns mapped currency codes`() = runTest(testDispatcher) {
        coEvery { api.getTickerCurrencies() } returns listOf("MXN", "EUR")

        val result = repository.getAvailableCurrencies()

        assertEquals(listOf(CurrencyCode.MXN, CurrencyCode.EUR), result)
    }

    @Test
    fun `GIVEN api returns unknown currency codes WHEN get available currencies THEN filters out unknown currencies`() = runTest(testDispatcher) {
        coEvery { api.getTickerCurrencies() } returns listOf("MXN", "UNKNOWN")

        val result = repository.getAvailableCurrencies()

        assertEquals(listOf(CurrencyCode.MXN), result)
    }

    @Test
    fun `GIVEN api throws exception WHEN get available currencies THEN returns fallback currencies`() = runTest(testDispatcher) {
        coEvery { api.getTickerCurrencies() } throws RuntimeException()

        val result = repository.getAvailableCurrencies()

        assertEquals(listOf(CurrencyCode.MXN, CurrencyCode.ARS, CurrencyCode.BRL, CurrencyCode.COP), result)
    }

    @Test
    fun `GIVEN api returns valid tickers WHEN get exchange rates THEN returns success with mapped models`() = runTest(testDispatcher) {
        coEvery { api.getTickers(any()) } returns listOf(
            CurrencyExchangeData(ask = "1.08", bid = "1.07", book = "USD_EUR", date = "2024-01-01")
        )

        val result = repository.getExchangeRates(listOf(CurrencyCode.EUR))

        assertEquals(
            CurrencyExchangeModel(CurrencyCode.EUR, ask = 1.08, bid = 1.07, date = "2024-01-01"),
            result.getOrNull()?.first()
        )
    }

    @Test
    fun `GIVEN api returns tickers with unknown books WHEN get exchange rates THEN filters out invalid entries`() = runTest(testDispatcher) {
        coEvery { api.getTickers(any()) } returns listOf(
            CurrencyExchangeData(ask = "1.08", bid = "1.07", book = "USD_EUR", date = "2024-01-01"),
            CurrencyExchangeData(ask = "1.0", bid = "1.0", book = "USD_UNKNOWN", date = "2024-01-01"),
        )

        val result = repository.getExchangeRates(listOf(CurrencyCode.EUR))

        assertEquals(1, result.getOrNull()?.size)
    }

    @Test
    fun `GIVEN api returns empty list WHEN get exchange rates THEN returns failure`() = runTest(testDispatcher) {
        coEvery { api.getTickers(any()) } returns emptyList()

        val result = repository.getExchangeRates(listOf(CurrencyCode.EUR))

        assertTrue(result.isFailure)
    }

    @Test
    fun `GIVEN api throws exception WHEN get exchange rates THEN returns failure`() = runTest(testDispatcher) {
        coEvery { api.getTickers(any()) } throws RuntimeException()

        val result = repository.getExchangeRates(listOf(CurrencyCode.EUR))

        assertTrue(result.isFailure)
    }

    @Test
    fun `GIVEN currencies list WHEN get exchange rates THEN passes joined api codes as query`() = runTest(testDispatcher) {
        coEvery { api.getTickers(any()) } returns listOf(
            CurrencyExchangeData(ask = "1.08", bid = "1.07", book = "USD_EUR", date = "2024-01-01")
        )

        repository.getExchangeRates(listOf(CurrencyCode.EUR, CurrencyCode.MXN))

        coVerify { api.getTickers("EUR,MXN") }
    }
}
