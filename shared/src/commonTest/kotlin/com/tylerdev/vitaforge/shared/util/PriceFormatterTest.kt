package com.tylerdev.vitaforge.shared.util

import kotlin.test.Test
import kotlin.test.assertEquals

class PriceFormatterTest {
    @Test
    fun formatAmount_wholeOrOneDecimal_padsToTwoDecimals() {
        val expectedResults = listOf("0.00", "349.00", "12.50")

        val actualResults = listOf(0.0, 349.0, 12.5).map(::formatAmount)

        assertEquals(expectedResults, actualResults)
    }

    @Test
    fun formatAmount_extraDecimals_roundsToTwoDecimals() {
        val expectedResults = listOf("10.13", "11.00")

        val actualResults = listOf(10.126, 10.999).map(::formatAmount)

        assertEquals(expectedResults, actualResults)
    }

    @Test
    fun formatAmount_thousands_addsGroupSeparators() {
        val expectedResults = listOf("1,299.00", "1,234,567.89")

        val actualResults = listOf(1299.0, 1234567.89).map(::formatAmount)

        assertEquals(expectedResults, actualResults)
    }
}
