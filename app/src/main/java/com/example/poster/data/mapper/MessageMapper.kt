package com.example.poster.data.mapper

import com.example.poster.data.remote.dto.message.MessageDto
import com.example.poster.data.remote.dto.message.AttachmentDto
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AttachmentType
import com.example.poster.domain.model.AttachmentUploadStatus
import com.example.poster.domain.model.Message
import com.example.poster.domain.model.MessageStatus

fun MessageDto.toDomain(): Message {
    return Message(
        id = id,
        chatId = chatId,
        text = text.orEmpty(),
        time = createdAt,
        isMine = isMine,
        status = status.toMessageStatus(),
        attachments = attachments.map { it.toDomain() },
    )
}

fun AttachmentDto.toDomain(): Attachment {
    return Attachment(
        id = id,
        localUri = null,
        remoteUrl = url,
        fileName = fileName,
        mimeType = mimeType,
        sizeBytes = sizeBytes,
        type = type.toAttachmentType(mimeType),
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

private fun String.toAttachmentType(mimeType: String): AttachmentType {
    return when {
        equals("IMAGE", ignoreCase = true) || mimeType.startsWith("image/") -> AttachmentType.IMAGE
        equals("VIDEO", ignoreCase = true) || mimeType.startsWith("video/") -> AttachmentType.VIDEO
        equals("AUDIO", ignoreCase = true) || mimeType.startsWith("audio/") -> AttachmentType.AUDIO
        equals("DOCUMENT", ignoreCase = true) || equals("FILE", ignoreCase = true) -> AttachmentType.DOCUMENT
        mimeType.contains("pdf") || mimeType.contains("text") -> AttachmentType.DOCUMENT
        else -> AttachmentType.UNKNOWN
    }
}
