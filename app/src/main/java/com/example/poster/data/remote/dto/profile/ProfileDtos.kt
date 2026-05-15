package com.example.poster.data.remote.dto.profile

data class ProfileDto(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val birthday: String?,
    val bio: String?,
    val avatarUrl: String?,
    val isOnline: Boolean,
)

data class UpdateProfileRequestDto(
    val name: String? = null,
    val username: String? = null,
    val bio: String? = null,
    val birthday: String? = null,
)

data class UploadAvatarResponseDto(
    val avatarUrl: String,
)
