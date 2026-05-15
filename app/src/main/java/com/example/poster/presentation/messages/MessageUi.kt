package com.example.poster.presentation.messages

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AttachmentType
import com.example.poster.domain.model.Message
import java.util.Locale

enum class MessageContentType {
    TEXT,
    FILE,
    IMAGE,
}

data class MessageAttachmentUi(
    val id: String,
    val fileName: String,
    val fileSize: String,
    val mimeType: String,
    val sizeBytes: Long = 0L,
    val localUri: String? = null,
    val remoteUrl: String? = null,
)

data class MessageUi(
    val id: String,
    val text: String = "",
    val time: String,
    val isMine: Boolean,
    val type: MessageContentType = MessageContentType.TEXT,
    val attachment: MessageAttachmentUi? = null,
)

fun Message.toMessageUi(): MessageUi {
    val firstAttachment = attachments.firstOrNull()
    return MessageUi(
        id = id,
        text = text,
        time = time,
        isMine = isMine,
        type = when (firstAttachment?.type) {
            AttachmentType.IMAGE -> MessageContentType.IMAGE
            null -> MessageContentType.TEXT
            else -> MessageContentType.FILE
        },
        attachment = firstAttachment?.toMessageAttachmentUi(),
    )
}

fun Attachment.toMessageAttachmentUi(): MessageAttachmentUi {
    return MessageAttachmentUi(
        id = id,
        fileName = fileName,
        fileSize = sizeBytes.toReadableFileSize(),
        sizeBytes = sizeBytes,
        mimeType = mimeType,
        localUri = localUri,
        remoteUrl = remoteUrl,
    )
}

fun Long.toReadableFileSize(): String {
    if (this <= 0L) {
        return "0 B"
    }

    val units = listOf("B", "KB", "MB", "GB")
    var value = toDouble()
    var index = 0

    while (value >= 1024 && index < units.lastIndex) {
        value /= 1024
        index += 1
    }

    return String.format(Locale.US, "%.1f %s", value, units[index])
}
