package com.vitaforge.data.domain

import com.tylerdev.vitaforge.shared.util.RequestState
import dev.gitlive.firebase.auth.FirebaseUser

interface CustomerRepository {
    fun getCurrentUserId(): String?

    suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun signOut(): RequestState<Unit>
}
