package com.example.poster.domain.usecase.message

import com.example.poster.domain.model.Attachment
import com.example.poster.domain.repository.MessageRepository

class DownloadAttachmentUseCase(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(attachmentId: String): Result<Attachment> {
        if (attachmentId.isBlank()) {
            return Result.failure(IllegalArgumentException("Attachment id is empty"))
        }

        return messageRepository.downloadAttachment(attachmentId)
    }
}
