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
        user = user.toDomain(),
        bio = bio,
        avatarUrl = avatarUrl,
        status = status,
    )
}

fun RemoteChatDto.toDomain(): Chat {
    return Chat(
        id = id,
        title = title,
        memberIds = memberIds,
        lastMessagePreview = lastMessagePreview,
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
        senderId = senderId,
        text = text,
        attachments = attachments.map(RemoteAttachmentDto::toDomain),
        timestampMillis = timestampMillis,
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
