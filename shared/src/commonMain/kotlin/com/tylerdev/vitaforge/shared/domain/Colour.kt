package com.tylerdev.vitaforge.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Colour(
    val id: String,
    val name: String,
)
