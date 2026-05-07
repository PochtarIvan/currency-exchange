package com.currencyexchange.model

internal enum class CurrencyCode(
    val apiCode: String,
    val symbol: String,
) {
    USD_C("USDc", "$"),
    MXN("MXN", "$"),
    EUR("EUR", "€"),
    BRL("BRL", "R$"),
    COP("COP", "$"),
    ARS("ARS", "$"),
}
internal fun String.toCurrencyCodeOrNull(): CurrencyCode? =
    CurrencyCode.entries.firstOrNull { code ->
        code.apiCode.equals(this, ignoreCase = true)
    }