package com.example.poster.domain.model

data class Chat(
    val id: String,
    val title: String,
    val initials: String,
    val lastMessage: String,
    val lastMessageTime: String,
    val unreadCount: Int,
)
