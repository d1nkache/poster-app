package com.example.poster.data.remote.websocket

enum class PosterSocketEventType {
    NEW_MESSAGE,
    MESSAGE_SENT,
    MESSAGE_READ,
    CHAT_UPDATED,
    SYNC_STARTED,
    SYNC_FINISHED,
    ERROR,
}

data class PosterSocketEventDto(
    val type: PosterSocketEventType,
    val payload: String,
)
