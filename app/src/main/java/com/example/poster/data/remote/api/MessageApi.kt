package com.example.poster.data.remote.api

import com.example.poster.data.remote.dto.message.MessageDto
import com.example.poster.data.remote.dto.message.MessageListResponseDto
import com.example.poster.data.remote.dto.message.SendMessageRequestDto

interface MessageApi {
    suspend fun getMessages(chatId: String): MessageListResponseDto
    suspend fun sendMessage(chatId: String, request: SendMessageRequestDto): MessageDto
    suspend fun deleteMessage(messageId: String)
    suspend fun markMessageAsRead(messageId: String)
}
