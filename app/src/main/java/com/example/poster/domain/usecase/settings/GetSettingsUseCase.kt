package com.example.poster.domain.usecase.settings

import com.example.poster.domain.model.UserSettings
import com.example.poster.domain.repository.SettingsRepository

class GetSettingsUseCase(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(): Result<UserSettings> {
        return settingsRepository.getSettings()
    }
}
