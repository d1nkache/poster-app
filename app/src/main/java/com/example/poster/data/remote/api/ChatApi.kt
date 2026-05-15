package com.example.poster.data.remote.api

import com.example.poster.data.remote.dto.chat.ChatDto
import com.example.poster.data.remote.dto.chat.ChatListResponseDto
import com.example.poster.data.remote.dto.chat.CreateChatRequestDto

interface ChatApi {
    suspend fun getChats(): ChatListResponseDto
    suspend fun getChat(chatId: String): ChatDto
    suspend fun createChat(request: CreateChatRequestDto): ChatDto
    suspend fun deleteChat(chatId: String)
    suspend fun markChatAsRead(chatId: String)
    suspend fun syncChats()
}
