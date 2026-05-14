package com.example.poster.domain.usecase.settings

import com.example.poster.domain.repository.SettingsRepository

class CheckMailAccessTokenUseCase(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(): Boolean {
        return settingsRepository.hasMailAccessToken()
    }
}
