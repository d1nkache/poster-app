package com.example.poster.data.mapper

import com.example.poster.data.remote.dto.chat.ChatDto
import com.example.poster.domain.model.Chat

fun ChatDto.toDomain(): Chat {
    return Chat(
        id = id,
        title = title,
        initials = initials,
        lastMessage = lastMessage.orEmpty(),
        lastMessageTime = lastMessageTime.orEmpty(),
        unreadCount = unreadCount,
    )
}
