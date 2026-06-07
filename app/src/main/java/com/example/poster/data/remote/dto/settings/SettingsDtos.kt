package com.example.poster.data.remote.dto.settings

data class SettingsDto(
    val language: String,
    val hasMailAccessToken: Boolean,
    val smtpHost: String? = null,
    val smtpPort: Int? = null,
    val imapHost: String? = null,
    val imapPort: Int? = null,
)

data class SaveMailAccessTokenRequestDto(
    val token: String,
    val smtpHost: String? = null,
    val smtpPort: Int? = null,
    val imapHost: String? = null,
    val imapPort: Int? = null,
)

data class MailAccessTokenStatusDto(
    val configured: Boolean,
)
