package com.tylerdev.vitaforge.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import vitaforge.shared.generated.resources.Res
import vitaforge.shared.generated.resources.bebas_neue_regular
import vitaforge.shared.generated.resources.roboto_condensed_medium

@Composable
fun bebasNeueFontFamily(): FontFamily =
    FontFamily(
        Font(Res.font.bebas_neue_regular),
    )

@Composable
fun robotoCondensedFontFamily(): FontFamily =
    FontFamily(
        Font(Res.font.roboto_condensed_medium),
    )

object FontSize {
    const val EXTRA_SMALL = 10
    const val SMALL = 12
    const val REGULAR = 14
    const val EXTRA_REGULAR = 16
    const val MEDIUM = 18
    const val EXTRA_MEDIUM = 20
    const val LARGE = 30
    const val EXTRA_LARGE = 40
}
