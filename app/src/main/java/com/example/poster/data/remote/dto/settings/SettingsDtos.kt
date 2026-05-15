package com.example.poster.data.remote.dto.settings

data class SettingsDto(
    val language: String,
    val hasMailAccessToken: Boolean,
)

data class SaveMailAccessTokenRequestDto(
    val token: String,
)

data class MailAccessTokenStatusDto(
    val configured: Boolean,
)
