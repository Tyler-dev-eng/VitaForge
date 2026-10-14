package com.tylerdev.vitaforge.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Flavour(
    val id: String,
    val name: String,
)
