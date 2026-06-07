package com.example.poster.domain.usecase.settings

import com.example.poster.domain.model.MailAccessSettings
import com.example.poster.domain.repository.SettingsRepository

class SaveMailAccessTokenUseCase(
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke(settings: MailAccessSettings): Result<Unit> {
        if (settings.token.isBlank()) {
            return Result.failure(IllegalArgumentException("Access token is empty"))
        }
        if (settings.smtpPort != null && settings.smtpPort !in 1..65535) {
            return Result.failure(IllegalArgumentException("SMTP port is invalid"))
        }
        if (settings.imapPort != null && settings.imapPort !in 1..65535) {
            return Result.failure(IllegalArgumentException("IMAP port is invalid"))
        }

        return settingsRepository.saveMailAccessToken(
            settings.copy(
                token = settings.token.trim(),
                smtpHost = settings.smtpHost?.trim()?.ifBlank { null },
                imapHost = settings.imapHost?.trim()?.ifBlank { null },
            )
        )
    }
}
