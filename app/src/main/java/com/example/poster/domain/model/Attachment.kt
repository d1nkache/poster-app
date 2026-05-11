package com.example.poster.domain.model

data class Attachment(
    val id: String,
    val type: AttachmentType,
    val url: String,
    val fileName: String,
)

enum class AttachmentType {
    IMAGE,
    VIDEO,
    FILE,
}
