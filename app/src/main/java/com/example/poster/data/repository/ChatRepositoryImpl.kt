package com.example.poster.data.repository

import com.example.poster.data.mapper.toDomain
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.model.Chat
import com.example.poster.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
) : ChatRepository {
    private val chatsFlow = MutableStateFlow<List<Chat>>(emptyList())

    override suspend fun getChats(): Result<List<Chat>> {
        val chats = remoteDataSource.getChats().map { it.toDomain() }
        chatsFlow.value = chats
        return Result.success(chats)
    }

    override suspend fun searchChats(query: String): Result<List<Chat>> {
        val chats = if (chatsFlow.value.isEmpty()) {
            remoteDataSource.getChats().map { it.toDomain() }
        } else {
            chatsFlow.value
        }
        val normalizedQuery = query.trim()
        val filteredChats = if (normalizedQuery.isBlank()) {
            chats
        } else {
            chats.filter {
                it.title.contains(normalizedQuery, ignoreCase = true) ||
                    it.lastMessage.contains(normalizedQuery, ignoreCase = true)
            }
        }
        return Result.success(filteredChats)
    }

    override suspend fun syncChats(): Result<Unit> {
        chatsFlow.value = remoteDataSource.getChats().map { it.toDomain() }
        return Result.success(Unit)
    }

    override fun observeChats(): Flow<List<Chat>> {
        return chatsFlow.asStateFlow()
    }

    override suspend fun markChatAsRead(chatId: String): Result<Unit> {
        chatsFlow.value = chatsFlow.value.map { chat ->
            if (chat.id == chatId) {
                chat.copy(unreadCount = 0)
            } else {
                chat
            }
        }
        return Result.success(Unit)
    }

    override suspend fun deleteChat(chatId: String): Result<Unit> {
        chatsFlow.value = chatsFlow.value.filterNot { it.id == chatId }
        return Result.success(Unit)
    }
}
