package com.example.poster.domain.repository

import com.example.poster.domain.model.Attachment

interface FileRepository {
    suspend fun getAttachmentInfo(uri: String): Result<Attachment>
    suspend fun copyToAppCache(uri: String): Result<Attachment>
    suspend fun saveToDownloads(attachment: Attachment): Result<Unit>
    suspend fun openAttachment(attachment: Attachment): Result<Unit>
}
