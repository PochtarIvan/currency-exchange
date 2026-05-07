package com.currencyexchange.viewmodel

import com.currencyexchange.data.repository.ExchangeRateRepository
import com.currencyexchange.model.CurrencyCode
import com.currencyexchange.model.CurrencyExchangeModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CurrencyViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: ExchangeRateRepository

    private val currencies = listOf(CurrencyCode.EUR, CurrencyCode.MXN)
    private val exchangeRates = listOf(
        CurrencyExchangeModel(CurrencyCode.EUR, ask = 1.08, bid = 1.07, date = "2024-01-01"),
        CurrencyExchangeModel(CurrencyCode.MXN, ask = 17.5, bid = 17.4, date = "2024-01-01"),
    )

    @Before
    fun setUp() {
        repository = mockk()
        Dispatchers.setMain(testDispatcher)
        coEvery { repository.getAvailableCurrencies() } returns currencies
        coEvery { repository.getExchangeRates(any()) } returns Result.success(exchangeRates)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN successful repository WHEN view model created THEN currencies are set`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)

        assertEquals(currencies, viewModel.uiState.currencies)
    }

    @Test
    fun `GIVEN successful repository WHEN view model created THEN target currency is first available`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)

        assertEquals(CurrencyCode.EUR, viewModel.uiState.targetCurrency)
    }

    @Test
    fun `GIVEN failing repository WHEN view model created THEN error overlay is set`() = runTest(testDispatcher) {
        coEvery { repository.getExchangeRates(any()) } returns Result.failure(RuntimeException())
        val viewModel = CurrencyViewModel(repository)

        assertEquals(Overlay.Error, viewModel.uiState.overlay)
    }

    @Test
    fun `GIVEN rates loaded WHEN source amount changed THEN source amount is updated`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)

        viewModel.onSourceAmountChanged("100")

        assertEquals("100", viewModel.uiState.sourceAmount)
    }

    @Test
    fun `GIVEN rates loaded WHEN source amount changed THEN target amount is calculated`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)

        viewModel.onSourceAmountChanged("100")

        assertEquals("108.00", viewModel.uiState.targetAmount)
    }

    @Test
    fun `GIVEN rates loaded WHEN target amount changed THEN source amount is calculated`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)

        viewModel.onTargetAmountChanged("108")

        assertEquals("100.00", viewModel.uiState.sourceAmount)
    }

    @Test
    fun `GIVEN rates loaded WHEN rate formatted requested THEN returns formatted string`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)

        assertEquals("1 USDc = 1.08 EUR", viewModel.rateFormatted)
    }

    @Test
    fun `GIVEN no currencies available WHEN rate formatted requested THEN returns empty`() = runTest(testDispatcher) {
        coEvery { repository.getAvailableCurrencies() } returns emptyList()
        coEvery { repository.getExchangeRates(any()) } returns Result.success(emptyList())
        val viewModel = CurrencyViewModel(repository)

        assertEquals("", viewModel.rateFormatted)
    }

    @Test
    fun `GIVEN rates loaded WHEN currency picker shown THEN currency picker overlay is set`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)

        viewModel.onCurrencyPickerShown()

        assertEquals(Overlay.CurrencyPicker, viewModel.uiState.overlay)
    }

    @Test
    fun `GIVEN currency picker open WHEN picker dismissed THEN overlay is cleared`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)
        viewModel.onCurrencyPickerShown()

        viewModel.onCurrencyPickerDismissed()

        assertEquals(null, viewModel.uiState.overlay)
    }

    @Test
    fun `GIVEN rates loaded WHEN target currency selected THEN target currency is updated`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)

        viewModel.onTargetCurrencySelected(CurrencyCode.MXN)

        assertEquals(CurrencyCode.MXN, viewModel.uiState.targetCurrency)
    }

    @Test
    fun `GIVEN default state WHEN swap clicked THEN action cells are reversed`() = runTest(testDispatcher) {
        val viewModel = CurrencyViewModel(repository)

        viewModel.onSwapClicked()

        assertEquals(
            listOf(CurrencyCellType.Target, CurrencyCellType.Source),
            viewModel.uiState.actionCells
        )
    }
}
