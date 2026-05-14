package com.example.poster.data.repository

import com.example.poster.data.mapper.toDomain
import com.example.poster.data.mapper.toRemote
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.Message
import com.example.poster.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessageRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
) : MessageRepository {
    private val messagesByChat = mutableMapOf<String, MutableStateFlow<List<Message>>>()

    override suspend fun getMessages(chatId: String): Result<List<Message>> {
        val messages = remoteDataSource
            .getMessages(chatId)
            .map { it.toDomain() }
        getOrCreateFlow(chatId).value = messages
        return Result.success(messages)
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return getOrCreateFlow(chatId).asStateFlow()
    }

    override suspend fun sendMessage(chatId: String, text: String, attachments: List<Attachment>): Result<Message> {
        val createdMessage = remoteDataSource
            .sendMessage(chatId, text, attachments.map { it.toRemote() })
            .toDomain()

        val currentMessages = getOrCreateFlow(chatId).value
        getOrCreateFlow(chatId).value = currentMessages + createdMessage
        return Result.success(createdMessage)
    }

    override suspend fun markMessageAsRead(messageId: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteMessage(messageId: String): Result<Unit> {
        messagesByChat.forEach { (_, flow) ->
            flow.value = flow.value.filterNot { it.id == messageId }
        }
        return Result.success(Unit)
    }

    private fun getOrCreateFlow(chatId: String): MutableStateFlow<List<Message>> {
        return messagesByChat.getOrPut(chatId) {
            MutableStateFlow(emptyList())
        }
    }
}
