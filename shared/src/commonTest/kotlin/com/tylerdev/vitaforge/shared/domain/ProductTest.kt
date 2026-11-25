package com.tylerdev.vitaforge.shared.domain

import kotlin.test.Test
import kotlin.test.assertEquals

private const val THUMBNAIL_URL = "https://example.com/thumbnail.png"
private const val BLACK_URL = "https://example.com/black.png"

class ProductTest {
    private fun createProduct(thumbnail: String) =
        Product(
            id = "id",
            title = "Shaker",
            description = "",
            thumbnail = thumbnail,
            category = ProductCategory.Accessories.name,
            colours = listOf("White", "Black"),
            colourImages = mapOf("Black" to BLACK_URL),
            price = 1.0,
        )

    @Test
    fun imageFor_colourWithImage_returnsColourImage() {
        val inputProduct = createProduct(thumbnail = THUMBNAIL_URL)

        val actualImage = inputProduct.imageFor(colour = "Black")

        assertEquals(BLACK_URL, actualImage)
    }

    @Test
    fun imageFor_colourWithoutImage_returnsThumbnail() {
        val inputProduct = createProduct(thumbnail = THUMBNAIL_URL)

        val actualImage = inputProduct.imageFor(colour = "White")

        assertEquals(THUMBNAIL_URL, actualImage)
    }

    @Test
    fun coverImage_noThumbnail_returnsFirstColourImage() {
        val inputProduct = createProduct(thumbnail = "")

        val actualImage = inputProduct.coverImage

        assertEquals(BLACK_URL, actualImage)
    }
}
