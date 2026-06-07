package com.example.poster.data.repository

import com.example.poster.domain.model.UserSettings
import com.example.poster.domain.model.MailAccessSettings
import com.example.poster.domain.repository.SettingsRepository

class MockSettingsRepository : SettingsRepository {
    private var hasMailAccessToken = false
    private var language = "English"

    override suspend fun getSettings(): Result<UserSettings> {
        return Result.success(
            UserSettings(
                name = "Your Name",
                username = "@your.username",
                email = "your.email@example.com",
                birthday = "January 1, 2000",
                bio = "Hey there! I'm using Poster.",
                language = language,
                hasMailAccessToken = hasMailAccessToken,
            )
        )
    }

    override suspend fun saveMailAccessToken(settings: MailAccessSettings): Result<Unit> {
        hasMailAccessToken = settings.token.isNotBlank()
        return Result.success(Unit)
    }

    override suspend fun hasMailAccessToken(): Boolean {
        return hasMailAccessToken
    }

    override suspend fun deleteMailAccessToken(): Result<Unit> {
        hasMailAccessToken = false
        return Result.success(Unit)
    }

    override suspend fun changeLanguage(languageCode: String): Result<Unit> {
        language = languageCode
        return Result.success(Unit)
    }
}
