package com.example.poster.data.repository

import android.content.Context
import android.provider.OpenableColumns
import com.example.poster.domain.model.Attachment
import com.example.poster.domain.model.AttachmentType
import com.example.poster.domain.model.AttachmentUploadStatus
import com.example.poster.domain.repository.FileRepository

class FileRepositoryImpl(
    private val context: Context,
) : FileRepository {
    override suspend fun getAttachmentInfo(uri: String): Result<Attachment> {
        val parsedUri = android.net.Uri.parse(uri)
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(parsedUri).orEmpty().ifBlank {
            "application/octet-stream"
        }

        var fileName = parsedUri.lastPathSegment ?: "attachment"
        var sizeBytes = 0L

        contentResolver.query(parsedUri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                if (nameIndex >= 0) {
                    fileName = cursor.getString(nameIndex) ?: fileName
                }
                if (sizeIndex >= 0) {
                    sizeBytes = cursor.getLong(sizeIndex)
                }
            }
        }

        return Result.success(
            Attachment(
                id = "local-${System.currentTimeMillis()}",
                localUri = uri,
                remoteUrl = null,
                fileName = fileName,
                mimeType = mimeType,
                sizeBytes = sizeBytes,
                type = mimeType.toAttachmentType(),
                uploadStatus = AttachmentUploadStatus.LOCAL_ONLY,
            )
        )
    }

    override suspend fun copyToAppCache(uri: String): Result<Attachment> {
        return getAttachmentInfo(uri)
    }

    override suspend fun saveToDownloads(attachment: Attachment): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun openAttachment(attachment: Attachment): Result<Unit> {
        return Result.success(Unit)
    }
}

private fun String.toAttachmentType(): AttachmentType {
    return when {
        startsWith("image/") -> AttachmentType.IMAGE
        startsWith("video/") -> AttachmentType.VIDEO
        startsWith("audio/") -> AttachmentType.AUDIO
        contains("pdf") || contains("document") || contains("text") -> AttachmentType.DOCUMENT
        else -> AttachmentType.UNKNOWN
    }
}
