package com.example.poster.data.remote.dto.message

data class MessageDto(
    val id: String,
    val chatId: String,
    val senderId: String,
    val text: String?,
    val createdAt: String,
    val isMine: Boolean,
    val status: String,
    val attachments: List<AttachmentDto> = emptyList(),
)

data class SendMessageRequestDto(
    val text: String,
    val attachmentIds: List<String> = emptyList(),
)

data class MessageListResponseDto(
    val messages: List<MessageDto>,
)

data class AttachmentDto(
    val id: String,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val url: String,
    val type: String,
)
