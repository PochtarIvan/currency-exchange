package com.currencyexchange.mapper

import com.currencyexchange.data.remote.CurrencyExchangeData
import com.currencyexchange.model.CurrencyExchangeModel

internal fun CurrencyExchangeData.toModel(): CurrencyExchangeModel =
    CurrencyExchangeModel(
        currency = book.substringAfter("_").uppercase(),
        ask = ask.toDouble(),
        bid = bid.toDouble(),
        date = date,
    )