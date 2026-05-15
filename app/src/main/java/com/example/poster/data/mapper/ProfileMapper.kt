package com.example.poster.data.mapper

import com.example.poster.data.remote.dto.profile.ProfileDto
import com.example.poster.domain.model.Profile

fun ProfileDto.toDomain(): Profile {
    return Profile(
        id = id,
        name = name,
        username = username,
        email = email,
        bio = bio.orEmpty(),
        avatarUrl = avatarUrl,
        isOnline = isOnline,
    )
}
