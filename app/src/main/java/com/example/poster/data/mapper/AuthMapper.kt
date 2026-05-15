package com.example.poster.data.mapper

import com.example.poster.data.remote.dto.auth.AuthResponseDto
import com.example.poster.domain.model.AuthSession

fun AuthResponseDto.toDomainSession(): AuthSession {
    return AuthSession(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = user.id,
    )
}
