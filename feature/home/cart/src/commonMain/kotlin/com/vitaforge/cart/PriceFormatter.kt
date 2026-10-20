package com.vitaforge.cart

import kotlin.math.roundToLong

private const val CENTS_PER_UNIT = 100
private const val GROUP_SIZE = 3
private const val GROUP_SEPARATOR = ','

internal fun formatAmount(amount: Double): String {
    val totalCents = (amount * CENTS_PER_UNIT).roundToLong()
    val units = (totalCents / CENTS_PER_UNIT).toString().reversed().chunked(GROUP_SIZE)
    val cents = (totalCents % CENTS_PER_UNIT).toString().padStart(length = 2, padChar = '0')
    return units.joinToString(separator = GROUP_SEPARATOR.toString()).reversed() + ".$cents"
}
