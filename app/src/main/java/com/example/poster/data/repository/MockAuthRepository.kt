package com.example.poster.data.repository

import com.example.poster.domain.model.AuthSession
import com.example.poster.domain.repository.AuthRepository

class MockAuthRepository : AuthRepository {
    private var session: AuthSession? = null

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        session = createSession(email)
        return Result.success(Unit)
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<Unit> {
        session = createSession(email)
        return Result.success(Unit)
    }

    override suspend fun verifyOtp(email: String, code: String): Result<Unit> {
        session = createSession(email)
        return Result.success(Unit)
    }

    override suspend fun resendOtp(email: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun logout(): Result<Unit> {
        session = null
        return Result.success(Unit)
    }

    override suspend fun isAuthorized(): Boolean {
        return session != null
    }

    override suspend fun refreshToken(): Result<Unit> {
        val currentSession = session
            ?: return Result.failure(IllegalStateException("No active session"))
        session = currentSession.copy(accessToken = "mock-access-refreshed")
        return Result.success(Unit)
    }

    override suspend fun getActiveSession(): AuthSession? {
        return session
    }

    private fun createSession(email: String): AuthSession {
        return AuthSession(
            accessToken = "mock-access-token",
            refreshToken = "mock-refresh-token",
            userId = "user-${email.hashCode()}",
        )
    }
}
