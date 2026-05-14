package com.example.poster.domain.model

data class Message(
    val id: String,
    val chatId: String,
    val text: String,
    val time: String,
    val isMine: Boolean,
    val status: MessageStatus,
    val attachments: List<Attachment> = emptyList(),
)
