package com.vitaforge.data

import kotlinx.coroutines.CancellationException

internal const val USER_NOT_AVAILABLE = "User is not available."

/** A failure whose message is already user-facing and must not be prefixed. */
internal class RepositoryException(
    message: String,
) : Exception(message)

/**
 * Runs [block] and wraps its outcome in a [Result]. Cancellation is always rethrown;
 * [RepositoryException]s keep their message, other failures are prefixed with [errorPrefix].
 */
internal inline fun <T> runRepository(
    errorPrefix: String,
    block: () -> T,
): Result<T> =
    try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: RepositoryException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(RepositoryException("$errorPrefix: ${e.message}"))
    }
