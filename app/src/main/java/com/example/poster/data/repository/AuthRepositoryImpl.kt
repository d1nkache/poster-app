package com.example.poster.data.repository

import com.example.poster.data.local.PosterLocalDataSource
import com.example.poster.data.mapper.toDomain
import com.example.poster.data.mapper.toLocal
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
    private val localDataSource: PosterLocalDataSource,
) : AuthRepository {
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        val session = remoteDataSource.login(email, password).toDomain()
        localDataSource.saveAuthSession(session.toLocal())
        return Result.success(Unit)
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
    ): Result<Unit> {
        val session = remoteDataSource.register(email, password, name).toDomain()
        localDataSource.saveAuthSession(session.toLocal())
        return Result.success(Unit)
    }

    override suspend fun verifyOtp(email: String, code: String): Result<Unit> {
        val session = remoteDataSource.verifyOtp(code).toDomain()
        localDataSource.saveAuthSession(session.toLocal())
        return Result.success(Unit)
    }

    override suspend fun resendOtp(email: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun logout(): Result<Unit> {
        localDataSource.clearAuthSession()
        return Result.success(Unit)
    }

    override suspend fun isAuthorized(): Boolean {
        return localDataSource.getAuthSession() != null
    }

    override suspend fun refreshToken(): Result<Unit> {
        return if (localDataSource.getAuthSession() != null) {
            Result.success(Unit)
        } else {
            Result.failure(IllegalStateException("No active session"))
        }
    }

    override suspend fun getActiveSession(): AuthSession? {
        return localDataSource.getAuthSession()?.toDomain()
    }
}
