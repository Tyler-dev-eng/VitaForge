package com.tylerdev.vitaforge.shared.domain

import androidx.compose.ui.graphics.Color
import com.tylerdev.vitaforge.shared.CategoryBlue
import com.tylerdev.vitaforge.shared.CategoryGreen
import com.tylerdev.vitaforge.shared.CategoryPurple
import com.tylerdev.vitaforge.shared.CategoryRed
import com.tylerdev.vitaforge.shared.CategoryYellow
import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class Product(
    val id: String,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String,
    val description: String,
    val thumbnail: String,
    val category: String,
    val flavours: List<String>? = null,
    val flavourImages: Map<String, String>? = null,
    val colours: List<String>? = null,
    val weight: Int? = null,
    val specsServing: String? = null,
    val specs: List<ProductSpec>? = null,
    val price: Double,
    val oldPrice: Double? = null,
    val isPopular: Boolean = false,
    val isDiscounted: Boolean = false,
    val isNew: Boolean = false,
) {
    /** The pre-discount price to show struck through, or null when the product isn't on sale. */
    val discountedFromPrice: Double?
        get() = oldPrice?.takeIf { isDiscounted && it > price }

    /** The main image: the thumbnail, or the first flavour's image when no thumbnail was added. */
    val coverImage: String
        get() =
            thumbnail.takeIf { it.isNotBlank() }
                ?: flavours
                    .orEmpty()
                    .firstNotNullOfOrNull { flavourImages?.get(it)?.takeIf(String::isNotBlank) }
                ?: ""

    /** The image for [flavour], falling back to [coverImage] when it has none. */
    fun imageFor(flavour: String?): String =
        flavour
            ?.let { flavourImages?.get(it) }
            ?.takeIf { it.isNotBlank() }
            ?: coverImage
}

@Serializable
data class ProductSpec(
    val label: String,
    val value: String,
)

enum class ProductCategory(
    val title: String,
    val colour: Color,
) {
    Protein(
        title = "Protein",
        colour = CategoryYellow,
    ),
    Creatine(
        title = "Creatine",
        colour = CategoryBlue,
    ),
    PreWorkout(
        title = "Pre-Workout",
        colour = CategoryGreen,
    ),
    Gainers(
        title = "Gainers",
        colour = CategoryPurple,
    ),
    Accessories(
        title = "Accessories",
        colour = CategoryRed,
    ),
}
