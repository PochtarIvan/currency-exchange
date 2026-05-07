package com.currencyexchange.mapper

import com.currencyexchange.data.remote.CurrencyExchangeData
import com.currencyexchange.model.CurrencyExchangeModel
import com.currencyexchange.model.toCurrencyCodeOrNull

internal fun CurrencyExchangeData.toModelOrNull(): CurrencyExchangeModel? {
    val currencyCode = book
        .substringAfter("_")
        .uppercase()
        .toCurrencyCodeOrNull() ?: return null
    val ask = ask.toDoubleOrNull() ?: return null
    val bid = bid.toDoubleOrNull() ?: return null

    return CurrencyExchangeModel(
        currency = currencyCode,
        ask = ask,
        bid = bid,
        date = date,
    )
}
