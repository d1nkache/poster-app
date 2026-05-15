package com.example.poster.data.remote.api

import com.example.poster.data.remote.dto.message.AttachmentDto

interface AttachmentApi {
    suspend fun uploadAttachment(chatId: String, localUri: String): AttachmentDto
    suspend fun getAttachment(attachmentId: String): AttachmentDto
    suspend fun deleteAttachment(attachmentId: String)
}
