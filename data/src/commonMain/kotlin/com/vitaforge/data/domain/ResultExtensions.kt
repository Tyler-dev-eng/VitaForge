package com.vitaforge.data.domain

private const val UNKNOWN_ERROR_MESSAGE = "Something went wrong."

/** User-facing message of a failed repository [Result]. */
fun Result<*>.errorMessage(): String = exceptionOrNull()?.message ?: UNKNOWN_ERROR_MESSAGE
