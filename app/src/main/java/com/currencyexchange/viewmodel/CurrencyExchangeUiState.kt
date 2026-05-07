package com.currencyexchange.viewmodel

import com.currencyexchange.model.CurrencyCode
import com.currencyexchange.model.CurrencyExchangeModel

internal data class CurrencyExchangeUiState(
    val overlay: Overlay? = null,
    val sourceAmount: String = "",
    val targetAmount: String = "",
    val sourceCurrency: CurrencyCode = CurrencyCode.USD_C,
    val targetCurrency: CurrencyCode? = null,
    val currencies: List<CurrencyCode> = emptyList(),
    val exchangeRates: List<CurrencyExchangeModel> = emptyList(),
    val actionCells: List<CurrencyCellType> = listOf(
        CurrencyCellType.Source,
        CurrencyCellType.Target
    ),
) {
    val exchangeRate: Double?
        get() = exchangeRates.firstOrNull {
            it.currency == targetCurrency?.apiCode
        }?.exchangeRate
}

internal data class CurrencyPickerItemState(
    val code: CurrencyCode,
    val isSelected: Boolean,
)

internal enum class CurrencyCellType {
    Source,
    Target
}

internal sealed interface Overlay {
    data object Loading : Overlay
    data object CurrencyPicker : Overlay
    data object Error : Overlay
}
