package com.example.poster.data.remote.api

import com.example.poster.data.remote.dto.settings.MailAccessTokenStatusDto
import com.example.poster.data.remote.dto.settings.SaveMailAccessTokenRequestDto
import com.example.poster.data.remote.dto.settings.SettingsDto

interface SettingsApi {
    suspend fun getSettings(): SettingsDto
    suspend fun updateSettings(language: String): SettingsDto
    suspend fun saveMailAccessToken(request: SaveMailAccessTokenRequestDto)
    suspend fun deleteMailAccessToken()
    suspend fun getMailAccessTokenStatus(): MailAccessTokenStatusDto
}
