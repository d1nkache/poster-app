package com.example.poster.data.remote.dto.chat

data class ChatDto(
    val id: String,
    val title: String,
    val initials: String,
    val lastMessage: String?,
    val lastMessageTime: String?,
    val unreadCount: Int,
    val isOnline: Boolean,
)

data class ChatListResponseDto(
    val items: List<ChatDto>,
)

data class CreateChatRequestDto(
    val recipient: String,
)
