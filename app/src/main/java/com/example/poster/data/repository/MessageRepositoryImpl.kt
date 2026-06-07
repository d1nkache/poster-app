package com.example.poster.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.poster.data.mapper.toDomain
import com.example.poster.data.mapper.toLocal
import com.example.poster.data.remote.PosterApiException
import com.example.poster.data.remote.PosterRemoteDataSource
import com.example.poster.data.remote.RemoteOutgoingAttachmentDto
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.Message
import com.example.poster.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessageRepositoryImpl(
    private val remoteDataSource: PosterRemoteDataSource,
    private val localDataSource: com.example.poster.data.local.PosterLocalDataSource,
    private val context: Context,
) : MessageRepository {
    private val messagesByChat = mutableMapOf<String, MutableStateFlow<List<Message>>>()

    override suspend fun getMessages(chatId: String): Result<List<Message>> {
        Log.d(TAG, "getMessages requested: chatId=$chatId")
        return runCatching {
            val messages = remoteDataSource
                .let { authorized { token -> it.getMessages(token, chatId) } }
                .map { it.toDomain() }
            getOrCreateFlow(chatId).value = messages
            Log.d(TAG, "getMessages succeeded: chatId=$chatId, count=${messages.size}")
            messages
        }.onFailure { error ->
            Log.e(TAG, "getMessages failed: chatId=$chatId", error)
        }
    }

    override fun observeMessages(chatId: String): Flow<List<Message>> {
        return getOrCreateFlow(chatId).asStateFlow()
    }

    override suspend fun sendMessage(chatId: String, text: String, attachments: List<Attachment>): Result<Message> {
        Log.d(TAG, "sendMessage requested: chatId=$chatId, textLength=${text.length}, attachments=${attachments.size}")
        return runCatching {
            val createdMessage = if (attachments.isEmpty()) {
                remoteDataSource
                    .let { authorized { token -> it.sendMessage(token, chatId, text) } }
                    .toDomain()
            } else {
                val outgoingAttachments = attachments.map { attachment ->
                    attachment.toRemoteOutgoingAttachment()
                }
                remoteDataSource
                    .let { authorized { token ->
                        it.sendMessageWithAttachments(
                            accessToken = token,
                            chatId = chatId,
                            text = text,
                            attachments = outgoingAttachments,
                        )
                    } }
                    .toDomain()
            }

            val currentMessages = getOrCreateFlow(chatId).value
            getOrCreateFlow(chatId).value = currentMessages + createdMessage
            Log.d(TAG, "sendMessage succeeded: chatId=$chatId, messageId=${createdMessage.id}")
            createdMessage
        }.onFailure { error ->
            Log.e(TAG, "sendMessage failed: chatId=$chatId", error)
        }
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
        Log.d(TAG, "markMessageAsRead requested: messageId=$messageId")
        return runCatching {
            authorized { remoteDataSource.markMessageAsRead(it, messageId) }
            Log.d(TAG, "markMessageAsRead succeeded: messageId=$messageId")
            Unit
        }.onFailure { error ->
            Log.e(TAG, "markMessageAsRead failed: messageId=$messageId", error)
        }
    }

    override suspend fun deleteMessage(messageId: String): Result<Unit> {
        Log.d(TAG, "deleteMessage requested: messageId=$messageId")
        return runCatching {
            authorized { remoteDataSource.deleteMessage(it, messageId) }
            messagesByChat.forEach { (_, flow) ->
                flow.value = flow.value.filterNot { it.id == messageId }
            }
            Log.d(TAG, "deleteMessage succeeded: messageId=$messageId")
            Unit
        }.onFailure { error ->
            Log.e(TAG, "deleteMessage failed: messageId=$messageId", error)
        }
    }

    private fun getOrCreateFlow(chatId: String): MutableStateFlow<List<Message>> {
        return messagesByChat.getOrPut(chatId) {
            MutableStateFlow(emptyList())
        }
    }

    private fun Attachment.toRemoteOutgoingAttachment(): RemoteOutgoingAttachmentDto {
        val localUri = requireNotNull(localUri) {
            "Only local attachments can be sent"
        }
        val bytes = context.contentResolver.openInputStream(Uri.parse(localUri))?.use { input ->
            input.readBytes()
        } ?: throw IllegalArgumentException("Cannot read attachment: $fileName")

        return RemoteOutgoingAttachmentDto(
            fileName = fileName,
            contentType = mimeType.ifBlank { "application/octet-stream" },
            bytes = bytes,
        )
    }

    private suspend fun accessToken(): String? {
        return localDataSource.getAuthSession()?.accessToken
    }

    private suspend fun <T> authorized(block: suspend (String?) -> T): T {
        return try {
            block(accessToken())
        } catch (error: PosterApiException) {
            if (error.statusCode != 401) throw error
            Log.d(TAG, "Access token expired, refreshing for message request")
            val currentSession = localDataSource.getAuthSession() ?: throw error
            val refreshedSession = remoteDataSource.refresh(currentSession.refreshToken).toDomain()
            localDataSource.saveAuthSession(refreshedSession.toLocal())
            block(refreshedSession.accessToken)
        }
    }

    private companion object {
        private const val TAG = "MessageRepository"
    }
}
