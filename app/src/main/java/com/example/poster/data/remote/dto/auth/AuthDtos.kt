package com.example.poster.data.remote.dto.auth

data class SignInRequestDto(
    val email: String,
    val password: String,
)

data class SignUpRequestDto(
    val email: String,
    val password: String,
    val displayName: String? = null,
)

data class RegisterResponseDto(
    val message: String,
)

data class VerifyOtpRequestDto(
    val email: String,
    val code: String,
)

data class RefreshRequestDto(
    val refreshToken: String,
)

data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val profile: UserDto,
)

data class UserDto(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val birthday: String? = null,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val isOnline: Boolean = false,
)
