package com.example.poster.domain.usecase.settings

import com.example.poster.domain.repository.SettingsRepository

class SaveMailAccessTokenUseCase(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(token: String): Result<Unit> {
        if (token.isBlank()) {
            return Result.failure(IllegalArgumentException("Access token is empty"))
        }

        return settingsRepository.saveMailAccessToken(token.trim())
    }
}
