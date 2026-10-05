package com.tylerdev.vitaforge.shared.domain

import com.tylerdev.vitaforge.shared.Resources
import org.jetbrains.compose.resources.DrawableResource

enum class Country(
    val dialCode: Int,
    val code: String,
    val flag: DrawableResource,
) {
    Serbia(
        dialCode = 381,
        code = "RS",
        flag = Resources.Flag.Serbia,
    ),
    SouthAfrica(
        dialCode = 27,
        code = "ZA",
        flag = Resources.Flag.SouthAfrica,
    ),
    UnitedKingdom(
        dialCode = 44,
        code = "GB",
        flag = Resources.Flag.UnitedKingdom,
    ),
    UnitedStates(
        dialCode = 1,
        code = "US",
        flag = Resources.Flag.UnitedStates,
    ),
    India(
        dialCode = 91,
        code = "IN",
        flag = Resources.Flag.India,
    ),
}
