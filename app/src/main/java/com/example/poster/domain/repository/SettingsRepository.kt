package com.example.poster.domain.repository

import com.example.poster.domain.model.UserSettings
import com.example.poster.domain.model.MailAccessSettings

interface SettingsRepository {
    suspend fun getSettings(): Result<UserSettings>
    suspend fun saveMailAccessToken(settings: MailAccessSettings): Result<Unit>
    suspend fun hasMailAccessToken(): Boolean
    suspend fun deleteMailAccessToken(): Result<Unit>
    suspend fun changeLanguage(languageCode: String): Result<Unit>
}
