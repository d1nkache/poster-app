package com.example.poster.data.remote.api

import com.example.poster.data.remote.dto.chat.ChatDto
interface ChatApi {
    suspend fun getOrCreateChat(contactId: Long): ChatDto
    suspend fun markChatAsRead(chatId: String)
}
