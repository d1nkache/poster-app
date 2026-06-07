package com.example.poster.data.mapper

import com.example.poster.data.local.LocalAuthSessionEntity
import com.example.poster.data.remote.RemoteAttachmentDto
import com.example.poster.data.remote.RemoteAuthSessionDto
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AttachmentType
import com.example.poster.domain.model.AttachmentUploadStatus
import com.example.poster.domain.model.AuthSession

fun RemoteAttachmentDto.toDomain(): Attachment {
    return Attachment(
        id = id,
        localUri = localUri,
        remoteUrl = remoteUrl,
        type = type.toAttachmentType(),
        fileName = fileName,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        uploadStatus = uploadStatus.toAttachmentUploadStatus(),
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
        type = type.name,
        localUri = localUri,
        remoteUrl = remoteUrl,
        fileName = fileName,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        uploadStatus = uploadStatus.name,
    )
}

private fun String.toAttachmentType(): AttachmentType {
    return when (uppercase()) {
        "IMAGE" -> AttachmentType.IMAGE
        "VIDEO" -> AttachmentType.VIDEO
        "AUDIO" -> AttachmentType.AUDIO
        "DOCUMENT", "FILE" -> AttachmentType.DOCUMENT
        else -> AttachmentType.UNKNOWN
    }
}

private fun String.toAttachmentUploadStatus(): AttachmentUploadStatus {
    return when (uppercase()) {
        "LOCAL_ONLY" -> AttachmentUploadStatus.LOCAL_ONLY
        "UPLOADING" -> AttachmentUploadStatus.UPLOADING
        "UPLOADED" -> AttachmentUploadStatus.UPLOADED
        "FAILED" -> AttachmentUploadStatus.FAILED
        else -> AttachmentUploadStatus.UPLOADED
    }
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
