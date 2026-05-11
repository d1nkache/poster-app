package com.example.poster.domain.model

data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
)
