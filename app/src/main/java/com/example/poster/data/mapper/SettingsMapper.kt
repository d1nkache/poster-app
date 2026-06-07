package com.example.poster.data.mapper

import com.example.poster.data.remote.dto.profile.ProfileDto
import com.example.poster.data.remote.dto.settings.SettingsDto
import com.example.poster.domain.model.UserSettings

fun SettingsDto.toDomain(profile: ProfileDto): UserSettings {
    return UserSettings(
        name = profile.name,
        username = profile.username,
        email = profile.email,
        birthday = profile.birthday.orEmpty(),
        bio = profile.bio.orEmpty(),
        language = language,
        hasMailAccessToken = hasMailAccessToken,
        smtpHost = smtpHost,
        smtpPort = smtpPort,
        imapHost = imapHost,
        imapPort = imapPort,
    )
}
