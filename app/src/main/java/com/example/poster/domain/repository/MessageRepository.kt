package com.example.poster.domain.repository

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun observeMessages(chatId: String): Flow<List<Message>>
    suspend fun refreshMessages(chatId: String)
    suspend fun sendMessage(chatId: String, text: String, attachments: List<Attachment> = emptyList())
}
