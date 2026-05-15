package com.example.poster.data.remote.dto.auth

data class SignInRequestDto(
    val email: String,
    val password: String,
)

data class SignUpRequestDto(
    val name: String,
    val email: String,
    val password: String,
)

data class VerifyOtpRequestDto(
    val email: String,
    val code: String,
)

data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto,
)

data class UserDto(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
)
