package com.currencyexchange.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currencyexchange.data.repository.ExchangeRateRepository
import com.currencyexchange.model.CurrencyCode
import com.currencyexchange.model.toCurrencyCodeOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
internal class CurrencyViewModel @Inject constructor(
    private val repository: ExchangeRateRepository,
) : ViewModel() {

    var uiState by mutableStateOf(
        CurrencyExchangeUiState(
            sourceCurrency = CurrencyCode.USD_C,
            targetCurrency = availableCurrencies.first(),
            currencies = availableCurrencies,
        )
    )
        private set

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        updateState { copy(overlay = Overlay.Loading) }

        viewModelScope.launch {
            val currencies = repository.getAvailableCurrencies()

            repository.getExchangeRates(currencies)
                .onSuccess { rates ->
                    updateState {
                        copy(
                            overlay = null,
                            currencies = currencies.mapNotNull { it.toCurrencyCodeOrNull() },
                            exchangeRates = rates,
                        )
                    }
                }
                .onFailure { _ ->
                    updateState {
                        copy(
                            overlay = Overlay.Error,
                        )
                    }
                }
        }
    }

    val rateFormatted: String
        get() {
            val rate = uiState.exchangeRate ?: return ""
            val formattedRate = formatNumber(rate)
            return "1 ${uiState.sourceCurrency.apiCode} = $formattedRate ${uiState.targetCurrency?.apiCode}"
        }

    val currencyPickerItems: ImmutableList<CurrencyPickerItemState>
        get() = uiState.currencies.map { currency ->
            CurrencyPickerItemState(
                code = currency,
                isSelected = currency == uiState.targetCurrency
            )
        }.toImmutableList()

    fun onSourceAmountChanged(value: String) {
        val normalized = normalizeAmountInput(value)

        updateState {
            copy(
                sourceAmount = normalized,
                targetAmount = calculateTargetAmount(
                    source = normalized,
                    rate = uiState.exchangeRate,
                )
            )
        }
    }

    fun onTargetAmountChanged(value: String) {
        val normalized = normalizeAmountInput(value)
        updateState {
            copy(
                targetAmount = normalized,
                sourceAmount = calculateSourceAmount(
                    target = normalized,
                    rate = uiState.exchangeRate,
                )
            )
        }
    }

    private fun calculateTargetAmount(
        source: String,
        rate: Double?,
    ): String {
        val amount = source.toDoubleOrNull() ?: return ""
        rate ?: return ""
        val result = amount * rate
        return formatNumber(result)
    }

    private fun calculateSourceAmount(
        target: String,
        rate: Double?,
    ): String {
        val amount = target.toDoubleOrNull() ?: return ""
        rate ?: return ""
        val result = amount / rate
        return formatNumber(result)
    }

    fun onCurrencyPickerShown() {
        updateState {
            copy(overlay = Overlay.CurrencyPicker)
        }
    }

    fun onCurrencyPickerDismissed() {
        updateState { copy(overlay = null) }
    }

    fun onTargetCurrencySelected(currency: CurrencyCode) {
        updateState {
            copy(
                targetCurrency = currency,
                overlay = null,
                targetAmount = calculateTargetAmount(
                    source = sourceAmount,
                    rate = findExchangeRate(currency)
                ),
            )
        }
    }

    fun onSwapClicked() {
        updateState {
            copy(
                actionCells = uiState.actionCells.reversed()
            )
        }
    }

    fun onRetryClicked() { loadInitialData() }

    private fun normalizeAmountInput(input: String): String {
        val builder = StringBuilder()
        var hasDot = false
        input.forEach { char ->
            when {
                char.isDigit() -> builder.append(char)
                char == '.' && !hasDot -> {
                    builder.append(char)
                    hasDot = true
                }
            }
        }

        return builder.toString()
    }

    private inline fun updateState(
        update: CurrencyExchangeUiState.() -> CurrencyExchangeUiState,
    ) {
        uiState = uiState.update()
    }

    private fun findExchangeRate(currency: CurrencyCode?): Double? {
        return uiState.exchangeRates.firstOrNull {
            it.currency == currency?.apiCode
        }?.exchangeRate
    }

    private fun formatNumber(value: Double): String {
        return NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }.format(value)
    }

    companion object {
        private val availableCurrencies = listOf(
            CurrencyCode.MXN,
            CurrencyCode.EUR,
            CurrencyCode.BRL,
            CurrencyCode.COP,
            CurrencyCode.ARS,
        )
    }
}