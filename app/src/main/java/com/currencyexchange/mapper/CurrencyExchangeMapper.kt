package com.currencyexchange.mapper

import com.currencyexchange.data.remote.CurrencyExchangeData
import com.currencyexchange.model.CurrencyExchangeModel
import com.currencyexchange.model.toCurrencyCodeOrNull

internal fun CurrencyExchangeData.toModelOrNull(): CurrencyExchangeModel? {
    val currencyCode = book
        .substringAfter("_")
        .uppercase()
        .toCurrencyCodeOrNull() ?: return null

    return CurrencyExchangeModel(
        currency = currencyCode,
        ask = ask.toDouble(),
        bid = bid.toDouble(),
        date = date,
    )
}
