package com.tylerdev.vitaforge.shared.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class SizeOrderTest {
    @Test
    fun sizeNameComparator_letterSizes_ordersSmallestFirst() {
        val inputSizes = listOf("XL", "m", "XXS", "L", "S")

        val actualSizes = inputSizes.sortedWith(SizeNameComparator)

        assertEquals(listOf("XXS", "S", "m", "L", "XL"), actualSizes)
    }

    @Test
    fun sizeNameComparator_numberedSizes_ordersByNumber() {
        val inputSizes = listOf("100 cm", "7.5 cm", "10 cm")

        val actualSizes = inputSizes.sortedWith(SizeNameComparator)

        assertEquals(listOf("7.5 cm", "10 cm", "100 cm"), actualSizes)
    }

    @Test
    fun sizeNameComparator_mixedSizes_ordersLettersThenNumbersThenNames() {
        val inputSizes = listOf("One size", "32", "M")

        val actualSizes = inputSizes.sortedWith(SizeNameComparator)

        assertEquals(listOf("M", "32", "One size"), actualSizes)
    }
}
