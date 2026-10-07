package com.tylerdev.vitaforge.shared.domain

import androidx.compose.ui.graphics.Color
import com.tylerdev.vitaforge.shared.CategoryBlue
import com.tylerdev.vitaforge.shared.CategoryGreen
import com.tylerdev.vitaforge.shared.CategoryPurple
import com.tylerdev.vitaforge.shared.CategoryRed
import com.tylerdev.vitaforge.shared.CategoryYellow
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val title: String,
    val description: String,
    val thumbnail: String,
    val category: String,
    val flavours: List<String>? = null,
    val weight: Int? = null,
    val price: Double,
    val isPopular: Boolean = false,
    val isDiscounted: Boolean = false,
    val isNew: Boolean = false,
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
