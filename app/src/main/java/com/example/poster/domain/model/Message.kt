package com.example.poster.domain.model

data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val text: String,
    val attachments: List<Attachment>,
    val timestampMillis: Long,
)
