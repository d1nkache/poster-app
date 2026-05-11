package com.example.poster.domain.repository

import com.example.poster.domain.model.AuthSession

interface AuthRepository {
    suspend fun login(identifier: String, password: String): AuthSession
    suspend fun register(identifier: String, password: String, displayName: String): AuthSession
    suspend fun verifyOtp(code: String): AuthSession
    suspend fun getActiveSession(): AuthSession?
    suspend fun clearSession()
}
