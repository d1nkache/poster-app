package com.example.poster.domain.model

data class UserSettings(
    val name: String,
    val username: String,
    val email: String,
    val birthday: String,
    val bio: String,
    val language: String,
    val hasMailAccessToken: Boolean,
)
