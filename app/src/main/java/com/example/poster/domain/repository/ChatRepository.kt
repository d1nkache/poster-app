package com.example.poster.domain.repository

import com.example.poster.domain.model.Chat
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChats(): Result<List<Chat>>
    suspend fun searchChats(query: String): Result<List<Chat>>
    suspend fun syncChats(): Result<Unit>
    fun observeChats(): Flow<List<Chat>>
    suspend fun markChatAsRead(chatId: String): Result<Unit>
    suspend fun deleteChat(chatId: String): Result<Unit>
}
