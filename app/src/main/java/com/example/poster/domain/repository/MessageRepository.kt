package com.example.poster.domain.repository

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getMessages(chatId: String): Result<List<Message>>
    fun observeMessages(chatId: String): Flow<List<Message>>
    suspend fun sendMessage(chatId: String, text: String, attachments: List<Attachment> = emptyList()): Result<Message>
    suspend fun markMessageAsRead(messageId: String): Result<Unit>
    suspend fun deleteMessage(messageId: String): Result<Unit>
}
