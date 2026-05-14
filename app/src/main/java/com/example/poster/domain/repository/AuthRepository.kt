package com.example.poster.domain.repository

import com.example.poster.domain.model.AuthSession

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<Unit>
    suspend fun signUp(name: String, email: String, password: String): Result<Unit>
    suspend fun verifyOtp(email: String, code: String): Result<Unit>
    suspend fun resendOtp(email: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun isAuthorized(): Boolean
    suspend fun refreshToken(): Result<Unit>

    suspend fun getActiveSession(): AuthSession?
}
