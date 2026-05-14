package com.example.poster.domain.repository

import com.example.poster.domain.model.UserSettings

interface SettingsRepository {
    suspend fun getSettings(): Result<UserSettings>
    suspend fun saveMailAccessToken(token: String): Result<Unit>
    suspend fun hasMailAccessToken(): Boolean
    suspend fun deleteMailAccessToken(): Result<Unit>
    suspend fun changeLanguage(languageCode: String): Result<Unit>
}
