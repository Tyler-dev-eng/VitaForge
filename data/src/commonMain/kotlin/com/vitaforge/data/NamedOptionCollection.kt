package com.vitaforge.data

import com.tylerdev.vitaforge.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

private const val FIELD_ID = "id"
private const val FIELD_NAME = "name"

/** A Firestore collection of uniquely named options that admins manage, such as flavours or colours. */
internal class NamedOptionCollection<T>(
    private val path: String,
    private val label: String,
    private val createOption: (id: String, name: String) -> T,
) {
    private val collection get() = Firebase.firestore.collection(collectionPath = path)

    fun observe(): Flow<RequestState<List<T>>> =
        collection.snapshots
            .map<_, RequestState<List<T>>> { query ->
                val options =
                    query.documents
                        .map { document -> document.id to document.get<String>(field = FIELD_NAME) }
                        .sortedBy { (_, name) -> name.lowercase() }
                        .map { (id, name) -> createOption(id, name) }
                RequestState.Success(options)
            }.catch { emit(RequestState.Error("Error while loading ${label.lowercase()}s. ${it.toFriendlyReason()}")) }

    suspend fun add(name: String): Result<T> {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) return Result.failure(IllegalArgumentException("$label name is empty."))
        return try {
            if (exists(trimmedName)) {
                Result.failure(IllegalArgumentException("$label already exists."))
            } else {
                Result.success(create(trimmedName))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Deletes every option called [name]; succeeds when there is none left to delete. */
    suspend fun delete(name: String): Result<Unit> =
        try {
            collection
                .get()
                .documents
                .filter { it.get<String>(field = FIELD_NAME).equals(name, ignoreCase = true) }
                .forEach { it.reference.delete() }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    private suspend fun exists(name: String): Boolean =
        collection
            .get()
            .documents
            .any { it.get<String>(field = FIELD_NAME).equals(name, ignoreCase = true) }

    private suspend fun create(name: String): T {
        val id = Uuid.random().toHexString()
        collection.document(id).set(mapOf(FIELD_ID to id, FIELD_NAME to name))
        return createOption(id, name)
    }
}
