package com.example.poster.domain.model

data class Profile(
    val user: User,
    val bio: String,
    val avatarUrl: String?,
    val status: String,
)
