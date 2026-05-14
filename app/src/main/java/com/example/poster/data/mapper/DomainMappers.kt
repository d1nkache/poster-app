package com.example.poster.data.mapper

import com.example.poster.data.local.LocalAuthSessionEntity
import com.example.poster.data.remote.RemoteAttachmentDto
import com.example.poster.data.remote.RemoteAuthSessionDto
import com.example.poster.data.remote.RemoteChatDto
import com.example.poster.data.remote.RemoteMessageDto
import com.example.poster.data.remote.RemoteProfileDto
import com.example.poster.data.remote.RemoteUserDto
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AuthSession
import com.example.poster.domain.model.Chat
import com.example.poster.domain.model.Message
import com.example.poster.domain.model.MessageStatus
import com.example.poster.domain.model.Profile
import com.example.poster.domain.model.User

fun RemoteUserDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        displayName = displayName,
    )
}

fun RemoteProfileDto.toDomain(): Profile {
    return Profile(
        id = user.id,
        name = user.displayName,
        username = user.username,
        email = "${user.username}@example.com",
        bio = bio,
        avatarUrl = avatarUrl,
        isOnline = status == "online",
    )
}

fun RemoteChatDto.toDomain(): Chat {
    return Chat(
        id = id,
        title = title,
        initials = title
            .split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .joinToString(separator = "") { it.first().uppercase() }
            .ifBlank { "P" },
        lastMessage = lastMessagePreview,
        lastMessageTime = "Now",
        unreadCount = unreadCount,
    )
}

fun RemoteAttachmentDto.toDomain(): Attachment {
    return Attachment(
        id = id,
        type = type,
        url = url,
        fileName = fileName,
    )
}

fun RemoteMessageDto.toDomain(): Message {
    return Message(
        id = id,
        chatId = chatId,
        text = text,
        time = "Now",
        isMine = senderId == "user-1",
        status = MessageStatus.SENT,
        attachments = attachments.map(RemoteAttachmentDto::toDomain),
    )
}

fun RemoteAuthSessionDto.toDomain(): AuthSession {
    return AuthSession(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
    )
}

fun Attachment.toRemote(): RemoteAttachmentDto {
    return RemoteAttachmentDto(
        id = id,
        type = type,
        url = url,
        fileName = fileName,
    )
}

fun AuthSession.toLocal(): LocalAuthSessionEntity {
    return LocalAuthSessionEntity(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
    )
}

fun LocalAuthSessionEntity.toDomain(): AuthSession {
    return AuthSession(
        accessToken = accessToken,
        refreshToken = refreshToken,
        userId = userId,
    )
}
