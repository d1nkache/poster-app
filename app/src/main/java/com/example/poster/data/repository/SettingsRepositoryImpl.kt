package com.example.poster.data.repository

import android.util.Log
import com.example.poster.data.local.PosterLocalDataSource
import com.example.poster.data.mapper.toDomain
import com.example.poster.data.mapper.toLocal
import com.example.poster.data.remote.PosterApiException
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.model.MailAccessSettings
import com.example.poster.domain.model.UserSettings
import com.example.poster.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
    private val localDataSource: PosterLocalDataSource,
) : SettingsRepository {
    override suspend fun getSettings(): Result<UserSettings> {
        Log.d(TAG, "getSettings requested")
        return runCatching {
            authorized { token ->
                remoteDataSource.getSettings(token).toDomain(
                    profile = remoteDataSource.getMyProfile(token),
                )
            }
        }.onSuccess { settings ->
            Log.d(TAG, "getSettings succeeded: hasMailAccessToken=${settings.hasMailAccessToken}")
        }.onFailure { error ->
            Log.e(TAG, "getSettings failed", error)
        }
    }

    override suspend fun saveMailAccessToken(settings: MailAccessSettings): Result<Unit> {
        Log.d(TAG, "saveMailAccessToken requested")
        return runCatching {
            authorized { remoteDataSource.saveMailAccessToken(it, settings) }
        }.onSuccess {
            Log.d(TAG, "saveMailAccessToken succeeded")
        }.onFailure { error ->
            Log.e(TAG, "saveMailAccessToken failed", error)
        }
    }

    override suspend fun hasMailAccessToken(): Boolean {
        Log.d(TAG, "hasMailAccessToken requested")
        return runCatching {
            authorized { remoteDataSource.hasMailAccessToken(it) }
        }.onSuccess { hasToken ->
            Log.d(TAG, "hasMailAccessToken succeeded: $hasToken")
        }.onFailure { error ->
            Log.e(TAG, "hasMailAccessToken failed", error)
        }.getOrDefault(false)
    }

    override suspend fun deleteMailAccessToken(): Result<Unit> {
        Log.d(TAG, "deleteMailAccessToken requested")
        return runCatching {
            authorized { remoteDataSource.deleteMailAccessToken(it) }
        }.onSuccess {
            Log.d(TAG, "deleteMailAccessToken succeeded")
        }.onFailure { error ->
            Log.e(TAG, "deleteMailAccessToken failed", error)
        }
    }

    override suspend fun changeLanguage(languageCode: String): Result<Unit> {
        Log.d(TAG, "changeLanguage requested: languageCode=$languageCode")
        return runCatching {
            authorized { remoteDataSource.changeLanguage(it, languageCode) }
        }.map { Unit }
            .onSuccess {
                Log.d(TAG, "changeLanguage succeeded: languageCode=$languageCode")
            }.onFailure { error ->
                Log.e(TAG, "changeLanguage failed: languageCode=$languageCode", error)
            }
    }

    private suspend fun accessToken(): String? {
        return localDataSource.getAuthSession()?.accessToken
    }

    private suspend fun <T> authorized(block: suspend (String?) -> T): T {
        return try {
            block(accessToken())
        } catch (error: PosterApiException) {
            if (error.statusCode != 401) throw error
            Log.d(TAG, "Access token expired, refreshing for settings request")
            val currentSession = localDataSource.getAuthSession() ?: throw error
            val refreshedSession = remoteDataSource.refresh(currentSession.refreshToken).toDomain()
            localDataSource.saveAuthSession(refreshedSession.toLocal())
            block(refreshedSession.accessToken)
        }
    }

    private companion object {
        private const val TAG = "SettingsRepository"
    }
}
