package com.example.poster.data.remote

import com.example.poster.domain.model.AttachmentType
import com.example.poster.domain.model.Profile
import kotlin.math.absoluteValue

data class RemoteUserDto(
    val id: String,
    val username: String,
    val displayName: String,
)

data class RemoteProfileDto(
    val user: RemoteUserDto,
    val bio: String,
    val avatarUrl: String?,
    val status: String,
)

data class RemoteChatDto(
    val id: String,
    val title: String,
    val memberIds: List<String>,
    val lastMessagePreview: String,
    val unreadCount: Int,
)

data class RemoteAttachmentDto(
    val id: String,
    val type: String,
    val localUri: String?,
    val remoteUrl: String?,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val uploadStatus: String,
)

data class RemoteMessageDto(
    val id: String,
    val chatId: String,
    val senderId: String,
    val text: String,
    val attachments: List<RemoteAttachmentDto>,
    val timestampMillis: Long,
)

data class RemoteAuthSessionDto(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
)

interface PosterRemoteDataSource {
    suspend fun login(identifier: String, password: String): RemoteAuthSessionDto
    suspend fun register(identifier: String, password: String, displayName: String): RemoteAuthSessionDto
    suspend fun verifyOtp(code: String): RemoteAuthSessionDto
    suspend fun getChats(): List<RemoteChatDto>
    suspend fun getMessages(chatId: String): List<RemoteMessageDto>
    suspend fun sendMessage(chatId: String, text: String, attachments: List<RemoteAttachmentDto>): RemoteMessageDto
    suspend fun getProfile(userId: String): RemoteProfileDto
    suspend fun updateProfile(profile: Profile): RemoteProfileDto
}

class StubPosterRemoteDataSource : PosterRemoteDataSource {
    override suspend fun login(identifier: String, password: String): RemoteAuthSessionDto {
        val userId = "user-${identifier.hashCode().absoluteValue}"
        return RemoteAuthSessionDto(
            accessToken = "access-$userId",
            refreshToken = "refresh-$userId",
            userId = userId,
        )
    }

    override suspend fun register(
        identifier: String,
        password: String,
        displayName: String,
    ): RemoteAuthSessionDto {
        val userId = "user-${displayName.hashCode().absoluteValue}"
        return RemoteAuthSessionDto(
            accessToken = "access-$userId",
            refreshToken = "refresh-$userId",
            userId = userId,
        )
    }

    override suspend fun verifyOtp(code: String): RemoteAuthSessionDto {
        val userId = "user-${code.hashCode().absoluteValue}"
        return RemoteAuthSessionDto(
            accessToken = "access-$userId",
            refreshToken = "refresh-$userId",
            userId = userId,
        )
    }

    override suspend fun getChats(): List<RemoteChatDto> {
        return listOf(
            RemoteChatDto(
                id = "chat-1",
                title = "Product Team",
                memberIds = listOf("user-1", "user-2", "user-3"),
                lastMessagePreview = "Let's sync the release notes",
                unreadCount = 2,
            ),
            RemoteChatDto(
                id = "chat-2",
                title = "Design Review",
                memberIds = listOf("user-1", "user-4"),
                lastMessagePreview = "Poster mockups are ready",
                unreadCount = 0,
            ),
        )
    }

    override suspend fun getMessages(chatId: String): List<RemoteMessageDto> {
        return listOf(
            RemoteMessageDto(
                id = "$chatId-msg-1",
                chatId = chatId,
                senderId = "user-2",
                text = "Initial message in $chatId",
                attachments = emptyList(),
                timestampMillis = System.currentTimeMillis() - 60_000L,
            ),
            RemoteMessageDto(
                id = "$chatId-msg-2",
                chatId = chatId,
                senderId = "user-1",
                text = "Architecture scaffold is ready for wiring",
                attachments = listOf(
                    RemoteAttachmentDto(
                        id = "attachment-1",
                        type = AttachmentType.DOCUMENT.name,
                        localUri = null,
                        remoteUrl = "https://example.com/spec.pdf",
                        fileName = "spec.pdf",
                        mimeType = "application/pdf",
                        sizeBytes = 248_000L,
                        uploadStatus = "UPLOADED",
                    )
                ),
                timestampMillis = System.currentTimeMillis(),
            ),
        )
    }

    override suspend fun sendMessage(
        chatId: String,
        text: String,
        attachments: List<RemoteAttachmentDto>,
    ): RemoteMessageDto {
        return RemoteMessageDto(
            id = "$chatId-msg-${System.currentTimeMillis()}",
            chatId = chatId,
            senderId = "user-1",
            text = text,
            attachments = attachments,
            timestampMillis = System.currentTimeMillis(),
        )
    }

    override suspend fun getProfile(userId: String): RemoteProfileDto {
        return RemoteProfileDto(
            user = RemoteUserDto(
                id = userId,
                username = "poster_user",
                displayName = "Poster User",
            ),
            bio = "Building the first clean architecture slice.",
            avatarUrl = null,
            status = "online",
        )
    }

    override suspend fun updateProfile(profile: Profile): RemoteProfileDto {
        return RemoteProfileDto(
            user = RemoteUserDto(
                id = profile.id,
                username = profile.username,
                displayName = profile.name,
            ),
            bio = profile.bio,
            avatarUrl = profile.avatarUrl,
            status = if (profile.isOnline) "online" else "offline",
        )
    }
}
