package com.vitaforge.data

import com.tylerdev.vitaforge.shared.domain.Flavour
import com.tylerdev.vitaforge.shared.util.RequestState
import com.vitaforge.data.domain.FlavourRepository
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlin.uuid.Uuid

private const val FLAVOUR_COLLECTION = "flavours"
private const val FIELD_NAME = "name"

internal class FlavourRepositoryImpl : FlavourRepository {
    private val collection get() = Firebase.firestore.collection(collectionPath = FLAVOUR_COLLECTION)

    override fun observeFlavours(): Flow<RequestState<List<Flavour>>> =
        collection.snapshots
            .map<_, RequestState<List<Flavour>>> { query ->
                val flavours =
                    query.documents
                        .map { document -> Flavour(id = document.id, name = document.get(field = FIELD_NAME)) }
                        .sortedBy { it.name.lowercase() }
                RequestState.Success(flavours)
            }.catch { emit(RequestState.Error("Error while loading flavours: ${it.message}")) }

    override suspend fun addFlavour(name: String): Result<Flavour> {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) return Result.failure(IllegalArgumentException("Flavour name is empty."))
        return try {
            val existingNames =
                collection
                    .get()
                    .documents
                    .map { it.get<String>(field = FIELD_NAME) }
            if (existingNames.any { it.equals(trimmedName, ignoreCase = true) }) {
                return Result.failure(IllegalArgumentException("Flavour already exists."))
            }
            val flavour = Flavour(id = Uuid.random().toHexString(), name = trimmedName)
            collection.document(flavour.id).set(flavour)
            Result.success(flavour)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

fun createFlavourRepository(): FlavourRepository = FlavourRepositoryImpl()
