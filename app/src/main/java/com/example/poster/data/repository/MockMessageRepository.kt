package com.example.poster.data.repository

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.Message
import com.example.poster.domain.model.MessageStatus
import com.example.poster.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockMessageRepository : MessageRepository {
    private val messagesByChat = mutableMapOf<String, MutableStateFlow<List<Message>>>()

    override suspend fun getMessages(chatId: String): Result<List<Message>> {
        val flow = getOrCreateFlow(chatId)
        return Result.success(flow.value)
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return getOrCreateFlow(chatId).asStateFlow()
    }

    override suspend fun sendMessage(
        chatId: String,
        text: String,
        attachments: List<Attachment>,
    ): Result<Message> {
        val message = Message(
            id = System.currentTimeMillis().toString(),
            chatId = chatId,
            text = text,
            time = "Now",
            isMine = true,
            status = MessageStatus.SENT,
            attachments = attachments,
        )
        val flow = getOrCreateFlow(chatId)
        flow.value = flow.value + message
        return Result.success(message)
    }

    override suspend fun uploadAttachment(chatId: String, attachment: Attachment): Result<Attachment> {
        return Result.success(attachment)
    }

    override suspend fun downloadAttachment(attachmentId: String): Result<Attachment> {
        val attachment = messagesByChat.values
            .flatMap { flow -> flow.value }
            .flatMap { message -> message.attachments }
            .firstOrNull { attachment -> attachment.id == attachmentId }
            ?: return Result.failure(IllegalArgumentException("Attachment not found"))

        return Result.success(attachment)
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
            MutableStateFlow(mockMessages(chatId))
        }
    }

    private fun mockMessages(chatId: String): List<Message> {
        return listOf(
            Message(
                id = "$chatId-1",
                chatId = chatId,
                text = "Hey! How are you?",
                time = "10:00 AM",
                isMine = false,
                status = MessageStatus.READ,
            ),
            Message(
                id = "$chatId-2",
                chatId = chatId,
                text = "I'm good, thanks! Just finished the project.",
                time = "10:05 AM",
                isMine = true,
                status = MessageStatus.READ,
            ),
        )
    }
}
