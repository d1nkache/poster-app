package com.example.poster.data.remote.dto.message

data class MessageDto(
    val id: Long,
    val chatId: Long,
    val bodyText: String,
    val direction: String,
    val status: String,
    val isRead: Boolean,
    val createdAt: String,
    val sentAt: String?,
    val receivedAt: String?,
    val attachments: List<MessageAttachmentDto> = emptyList(),
)

data class SendMessageRequestDto(
    val bodyText: String,
)

data class MessageListResponseDto(
    val items: List<MessageDto>,
)

data class MessageAttachmentDto(
    val id: Long,
    val url: String,
    val fileName: String?,
    val contentType: String,
    val sizeBytes: Long,
    val createdAt: String,
)

typealias AttachmentDto = MessageAttachmentDto
