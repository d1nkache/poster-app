package com.example.poster.data.repository

import com.example.poster.data.local.PosterLocalDataSource
import com.example.poster.data.mapper.toDomain
import com.example.poster.data.mapper.toLocal
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.model.AuthSession
import com.example.poster.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
    private val localDataSource: PosterLocalDataSource,
) : AuthRepository {
    override suspend fun login(identifier: String, password: String): AuthSession {
        val session = remoteDataSource.login(identifier, password).toDomain()
        localDataSource.saveAuthSession(session.toLocal())
        return session
    }

    override suspend fun register(
        identifier: String,
        password: String,
        displayName: String,
    ): AuthSession {
        val session = remoteDataSource.register(identifier, password, displayName).toDomain()
        localDataSource.saveAuthSession(session.toLocal())
        return session
    }

    override suspend fun verifyOtp(code: String): AuthSession {
        val session = remoteDataSource.verifyOtp(code).toDomain()
        localDataSource.saveAuthSession(session.toLocal())
        return session
    }

    override suspend fun getActiveSession(): AuthSession? {
        return localDataSource.getAuthSession()?.toDomain()
    }

    override suspend fun clearSession() {
        localDataSource.clearAuthSession()
    }
}
