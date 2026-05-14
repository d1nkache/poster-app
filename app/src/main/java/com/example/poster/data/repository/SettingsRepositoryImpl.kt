package com.example.poster.data.repository

import com.example.poster.domain.model.UserSettings
import com.example.poster.domain.repository.SettingsRepository

class SettingsRepositoryImpl : SettingsRepository {
    private var mailAccessToken: String? = null
    private var languageCode: String = "en"

    override suspend fun getSettings(): Result<UserSettings> {
        return Result.success(
            UserSettings(
                name = "Your Name",
                username = "@your.username",
                email = "your.email@example.com",
                birthday = "January 1, 2000",
                bio = "Hey there! I'm using Poster.",
                language = languageCode,
                hasMailAccessToken = mailAccessToken != null,
            )
        )
    }

    override suspend fun saveMailAccessToken(token: String): Result<Unit> {
        mailAccessToken = token
        return Result.success(Unit)
    }

    override suspend fun hasMailAccessToken(): Boolean {
        return mailAccessToken != null
    }

    override suspend fun deleteMailAccessToken(): Result<Unit> {
        mailAccessToken = null
        return Result.success(Unit)
    }

    override suspend fun changeLanguage(languageCode: String): Result<Unit> {
        this.languageCode = languageCode
        return Result.success(Unit)
    }
}
