package com.tylerdev.vitaforge

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
