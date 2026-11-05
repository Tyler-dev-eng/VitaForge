package com.vitaforge.manage_product

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InputFiltersTest {
    @Test
    fun parseWeightInput_emptyText_returnsNull() {
        val actualWeight = parseWeightInput("")

        assertNull(actualWeight)
    }

    @Test
    fun parseWeightInput_nonDigits_areStripped() {
        val actualWeight = parseWeightInput("-2.5kg")

        assertEquals(25, actualWeight)
    }

    @Test
    fun isValidPriceInput_inProgressDecimal_returnsTrue() {
        val actualResults = listOf("", "12", "12.", "12.5", "12.50", ".5").map(::isValidPriceInput)

        assertTrue(actualResults.all { it })
    }

    @Test
    fun isValidPriceInput_invalidText_returnsFalse() {
        val actualResults = listOf("-1", "1e5", "NaN", "1.2.3", "12.555", ".", "abc").map(::isValidPriceInput)

        assertFalse(actualResults.any { it })
    }
}
