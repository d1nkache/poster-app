package com.example.poster.data.mapper

import com.example.poster.core.network.NetworkConfig
import com.example.poster.data.remote.dto.message.MessageAttachmentDto
import com.example.poster.data.remote.dto.message.MessageDto
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AttachmentType
import com.example.poster.domain.model.AttachmentUploadStatus
import com.example.poster.domain.model.Message
import com.example.poster.domain.model.MessageStatus

fun MessageDto.toDomain(): Message {
    return Message(
        id = id.toString(),
        chatId = chatId.toString(),
        text = bodyText,
        time = createdAt,
        isMine = direction.equals("OUTGOING", ignoreCase = true),
        status = status.toMessageStatus(),
        attachments = attachments.map { it.toDomain() },
    )
}

fun MessageAttachmentDto.toDomain(): Attachment {
    val mimeType = contentType.ifBlank { "application/octet-stream" }
    return Attachment(
        id = id.toString(),
        localUri = null,
        remoteUrl = url.toAbsoluteAttachmentUrl(),
        fileName = fileName ?: url.substringAfterLast('/').ifBlank { "attachment" },
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        type = mimeType.toAttachmentType(),
        uploadStatus = AttachmentUploadStatus.UPLOADED,
    )
}

private fun String.toMessageStatus(): MessageStatus {
    return when (uppercase()) {
        "SENDING" -> MessageStatus.SENDING
        "SENT" -> MessageStatus.SENT
        "DELIVERED" -> MessageStatus.DELIVERED
        "READ" -> MessageStatus.READ
        "FAILED" -> MessageStatus.FAILED
        else -> MessageStatus.SENT
    }
}

private fun String.toAttachmentType(): AttachmentType {
    return when {
        startsWith("image/") -> AttachmentType.IMAGE
        startsWith("video/") -> AttachmentType.VIDEO
        startsWith("audio/") -> AttachmentType.AUDIO
        contains("pdf") || contains("text") -> AttachmentType.DOCUMENT
        else -> AttachmentType.UNKNOWN
    }
}

private fun String.toAbsoluteAttachmentUrl(): String {
    return if (startsWith("http://") || startsWith("https://")) {
        this
    } else {
        NetworkConfig.BASE_URL.trimEnd('/') + "/" + trimStart('/')
    }
}
