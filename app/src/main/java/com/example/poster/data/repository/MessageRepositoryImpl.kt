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

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return getOrCreateFlow(chatId).asStateFlow()
    }

    override suspend fun refreshMessages(chatId: String) {
        getOrCreateFlow(chatId).value = remoteDataSource
            .getMessages(chatId)
            .map { it.toDomain() }
    }

    override suspend fun sendMessage(chatId: String, text: String, attachments: List<Attachment>) {
        val createdMessage = remoteDataSource
            .sendMessage(chatId, text, attachments.map { it.toRemote() })
            .toDomain()

        val currentMessages = getOrCreateFlow(chatId).value
        getOrCreateFlow(chatId).value = currentMessages + createdMessage
    }

    private fun getOrCreateFlow(chatId: String): MutableStateFlow<List<Message>> {
        return messagesByChat.getOrPut(chatId) {
            MutableStateFlow(emptyList())
        }
    }
}
