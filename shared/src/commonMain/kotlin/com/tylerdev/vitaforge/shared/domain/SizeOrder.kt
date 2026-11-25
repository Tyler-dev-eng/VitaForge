package com.tylerdev.vitaforge.shared.domain

private val LETTER_SIZES = listOf("XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL")
private val LEADING_NUMBER = Regex("""^\d+([.,]\d+)?""")

/** Orders sizes the way shoppers expect: XXS to XXXL, then by leading number, then alphabetically. */
val SizeNameComparator: Comparator<String> =
    compareBy<String> { letterRank(it) }
        .thenBy { leadingNumber(it) }
        .thenBy { it.lowercase() }

private fun letterRank(name: String): Int {
    val rank = LETTER_SIZES.indexOf(name.trim().uppercase())
    return if (rank >= 0) rank else LETTER_SIZES.size
}

private fun leadingNumber(name: String): Double =
    LEADING_NUMBER
        .find(name.trim())
        ?.value
        ?.replace(',', '.')
        ?.toDouble() ?: Double.MAX_VALUE
