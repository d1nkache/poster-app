package com.example.poster.domain.model

data class Chat(
    val id: String,
    val title: String,
    val memberIds: List<String>,
    val lastMessagePreview: String,
    val unreadCount: Int,
)
