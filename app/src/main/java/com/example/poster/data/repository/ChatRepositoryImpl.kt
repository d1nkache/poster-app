package com.example.poster.data.repository

import android.util.Log
import com.example.poster.data.mapper.toDomain
import com.example.poster.data.mapper.toLocal
import com.example.poster.data.remote.PosterApiException
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.model.Chat
import com.example.poster.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
    private val localDataSource: com.example.poster.data.local.PosterLocalDataSource,
) : ChatRepository {
    private val chatsFlow = MutableStateFlow<List<Chat>>(emptyList())

    override suspend fun getChats(): Result<List<Chat>> {
        Log.d(TAG, "getChats requested")
        return runCatching {
            val chats = authorized { remoteDataSource.getChats(it) }.map { it.toDomain() }
            chatsFlow.value = chats
            Log.d(TAG, "getChats succeeded: count=${chats.size}")
            chats
        }.onFailure { error ->
            Log.e(TAG, "getChats failed", error)
        }
    }

    override suspend fun searchChats(query: String): Result<List<Chat>> {
        Log.d(TAG, "searchChats requested: queryLength=${query.length}")
        val chats = if (chatsFlow.value.isEmpty()) {
            authorized { remoteDataSource.getChats(it) }.map { it.toDomain() }
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
        Log.d(TAG, "syncChats requested")
        return runCatching {
            chatsFlow.value = authorized { remoteDataSource.getChats(it) }.map { it.toDomain() }
            Log.d(TAG, "syncChats succeeded: count=${chatsFlow.value.size}")
            Unit
        }.onFailure { error ->
            Log.e(TAG, "syncChats failed", error)
        }
    }

    override fun observeChats(): Flow<List<Chat>> {
        return chatsFlow.asStateFlow()
    }

    override suspend fun markChatAsRead(chatId: String): Result<Unit> {
        Log.d(TAG, "markChatAsRead requested: chatId=$chatId")
        return runCatching {
            authorized { remoteDataSource.markChatAsRead(it, chatId) }
            chatsFlow.value = chatsFlow.value.map { chat ->
                if (chat.id == chatId) {
                    chat.copy(unreadCount = 0)
                } else {
                    chat
                }
            }
            Log.d(TAG, "markChatAsRead succeeded: chatId=$chatId")
            Unit
        }.onFailure { error ->
            Log.e(TAG, "markChatAsRead failed: chatId=$chatId", error)
        }
    }

    override suspend fun deleteChat(chatId: String): Result<Unit> {
        chatsFlow.value = chatsFlow.value.filterNot { it.id == chatId }
        return Result.success(Unit)
    }

    private suspend fun accessToken(): String? {
        return localDataSource.getAuthSession()?.accessToken
    }

    private suspend fun <T> authorized(block: suspend (String?) -> T): T {
        return try {
            block(accessToken())
        } catch (error: PosterApiException) {
            if (error.statusCode != 401) throw error
            Log.d(TAG, "Access token expired, refreshing for chat request")
            val currentSession = localDataSource.getAuthSession() ?: throw error
            val refreshedSession = remoteDataSource.refresh(currentSession.refreshToken).toDomain()
            localDataSource.saveAuthSession(refreshedSession.toLocal())
            block(refreshedSession.accessToken)
        }
    }

    private companion object {
        private const val TAG = "ChatRepository"
    }
}
