package com.vitaforge.manage_product

private const val MAX_WEIGHT_DIGITS = 6
private const val MAX_PRICE_DECIMALS = 2
private const val MAX_PRICE_INTEGER_DIGITS = 7
private const val DECIMAL_SEPARATOR = '.'

/** Keeps digits only, so the weight can be cleared and never becomes negative or fractional. */
internal fun parseWeightInput(input: String): Int? =
    input
        .filter(Char::isDigit)
        .take(MAX_WEIGHT_DIGITS)
        .toIntOrNull()

/** True for in-progress price text such as "", "12", "12." or "12.5" (max 2 decimals, no sign or exponent). */
internal fun isValidPriceInput(input: String): Boolean {
    if (input.isEmpty()) return true
    val integerPart = input.substringBefore(DECIMAL_SEPARATOR)
    val hasSeparator = input.contains(DECIMAL_SEPARATOR)
    val decimalPart = input.substringAfter(DECIMAL_SEPARATOR, missingDelimiterValue = "")
    return integerPart.all(Char::isDigit) &&
        integerPart.length <= MAX_PRICE_INTEGER_DIGITS &&
        decimalPart.all(Char::isDigit) &&
        decimalPart.length <= MAX_PRICE_DECIMALS &&
        (integerPart.isNotEmpty() || decimalPart.isNotEmpty() || !hasSeparator)
}
