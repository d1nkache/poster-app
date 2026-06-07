package com.example.poster.data.repository

import android.util.Log
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
    override suspend fun signIn(email: String, password: String): Result<Unit> {
        Log.d(TAG, "signIn requested: email=$email")
        return runCatching {
            val session = remoteDataSource.login(email, password).toDomain()
            localDataSource.saveAuthSession(session.toLocal())
            Log.d(TAG, "signIn succeeded: userId=${session.userId}")
            Unit
        }.onFailure { error ->
            Log.e(TAG, "signIn failed: email=$email", error)
        }
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
    ): Result<Unit> {
        Log.d(TAG, "signUp requested: email=$email")
        return runCatching {
            remoteDataSource.register(email, password, name)
            Log.d(TAG, "signUp succeeded: email=$email")
            Unit
        }.onFailure { error ->
            Log.e(TAG, "signUp failed: email=$email", error)
        }
    }

    override suspend fun verifyOtp(email: String, code: String): Result<Unit> {
        Log.d(TAG, "verifyOtp requested: email=$email")
        return runCatching {
            val session = remoteDataSource.verifyOtp(email, code).toDomain()
            localDataSource.saveAuthSession(session.toLocal())
            Log.d(TAG, "verifyOtp succeeded: userId=${session.userId}")
            Unit
        }.onFailure { error ->
            Log.e(TAG, "verifyOtp failed: email=$email", error)
        }
    }

    override suspend fun resendOtp(email: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun logout(): Result<Unit> {
        Log.d(TAG, "logout requested")
        return runCatching {
            remoteDataSource.logout(localDataSource.getAuthSession()?.accessToken)
            localDataSource.clearAuthSession()
            Log.d(TAG, "logout succeeded")
            Unit
        }.onFailure { error ->
            Log.e(TAG, "logout failed", error)
        }
    }

    override suspend fun isAuthorized(): Boolean {
        return localDataSource.getAuthSession() != null
    }

    override suspend fun refreshToken(): Result<Unit> {
        Log.d(TAG, "refreshToken requested")
        return runCatching {
            val currentSession = localDataSource.getAuthSession()
                ?: throw IllegalStateException("No active session")
            val refreshedSession = remoteDataSource.refresh(currentSession.refreshToken).toDomain()
            localDataSource.saveAuthSession(refreshedSession.toLocal())
            Log.d(TAG, "refreshToken succeeded: userId=${refreshedSession.userId}")
            Unit
        }.onFailure { error ->
            Log.e(TAG, "refreshToken failed", error)
        }
    }

    override suspend fun getActiveSession(): AuthSession? {
        return localDataSource.getAuthSession()?.toDomain()
    }

    private companion object {
        private const val TAG = "AuthRepository"
    }
}
