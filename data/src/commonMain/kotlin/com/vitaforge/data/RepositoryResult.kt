package com.vitaforge.data

import kotlinx.coroutines.CancellationException

internal const val USER_NOT_AVAILABLE = "User is not available."

/** A failure whose message is already user-facing and must not be prefixed. */
internal class RepositoryException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause)

private const val PERMISSION_DENIED_MESSAGE = "You don't have permission to do this."
private const val NETWORK_MESSAGE = "Please check your internet connection and try again."
private const val SIGNED_OUT_MESSAGE = "Please sign in again and try once more."
private const val GENERIC_MESSAGE = "Something went wrong. Please try again."

/** Turns an SDK failure into a short reason that is safe to show to users. */
internal fun Throwable.toFriendlyReason(): String {
    val text = message.orEmpty()
    return when {
        text.contains("PERMISSION_DENIED", ignoreCase = true) -> PERMISSION_DENIED_MESSAGE
        text.contains("UNAUTHENTICATED", ignoreCase = true) -> SIGNED_OUT_MESSAGE
        text.contains("UNAVAILABLE", ignoreCase = true) ||
            text.contains("network", ignoreCase = true) ||
            text.contains("offline", ignoreCase = true) ||
            text.contains("DEADLINE_EXCEEDED", ignoreCase = true) -> NETWORK_MESSAGE
        else -> GENERIC_MESSAGE
    }
}

/**
 * Runs [block] and wraps its outcome in a [Result]. Cancellation is always rethrown;
 * [RepositoryException]s keep their message, other failures become "[errorPrefix]. <friendly reason>" so raw SDK text never reaches the UI.
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
        Result.failure(RepositoryException("$errorPrefix. ${e.toFriendlyReason()}", cause = e))
    }
