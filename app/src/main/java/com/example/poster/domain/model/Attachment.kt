package com.example.poster.domain.model

data class Attachment(
    val id: String,
    val localUri: String?,
    val remoteUrl: String?,
    val type: AttachmentType,
    val fileName: String,
    val mimeType: String,
    val sizeBytes: Long,
    val uploadStatus: AttachmentUploadStatus,
)

enum class AttachmentType {
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT,
    UNKNOWN,
}

enum class AttachmentUploadStatus {
    LOCAL_ONLY,
    UPLOADING,
    UPLOADED,
    FAILED,
}
